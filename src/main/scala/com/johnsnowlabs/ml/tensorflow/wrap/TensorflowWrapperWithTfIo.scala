/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.johnsnowlabs.ml.tensorflow.wrap

import com.johnsnowlabs.ml.tensorflow.TensorResources
import com.johnsnowlabs.ml.tensorflow.sentencepiece.LoadSentencepiece
import com.johnsnowlabs.ml.tensorflow.sign.ModelSignatureManager
import com.johnsnowlabs.ml.tensorflow.wrap.TensorflowWrapperWithTfIo._
import com.johnsnowlabs.nlp.annotators.ner.dl.LoadsContrib
import com.johnsnowlabs.nlp.util.io.ResourceHelper
import com.johnsnowlabs.util.{FileHelper, ZipArchiveUtil}
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.WildcardFileFilter
import org.apache.hadoop.fs.Path
import org.slf4j.{Logger, LoggerFactory}
import org.tensorflow._
import org.tensorflow.exceptions.TensorFlowException
import org.tensorflow.op.Ops
import org.tensorflow.proto.framework.{ConfigProto, GraphDef}
import org.tensorflow.types.TString

import java.io._
import java.net.URI
import java.nio.file.{Files, Paths}
import java.util.UUID
import scala.util.{Failure, Success, Try}


class TensorflowWrapperWithTfIo(var variables: VariablesTfIo,
                                var graph: Array[Byte]) extends Serializable with TFWrapper[TensorflowWrapperWithTfIo] {

  lazy val useTFIO = true

  /** For Deserialization */
  def this() =
    this(null, null)

  @transient private var m_session: Session = _
  @transient private val logger = LoggerFactory.getLogger("TensorflowWrapperWithTfIo")

  private def writeModelTensorsToFiles(folder: String) = {
    import org.tensorflow.op.Ops
    import org.tensorflow.types.TString
    val tf = Ops.create

    val varDataPath = Paths.get(folder, TensorflowWrapper.VariablesPathValue)
    val varDataTensor: TString = tf.io.readFile(tf.constant(varDataPath.toAbsolutePath.toString)).asTensor
    tf.io.writeFile(tf.constant(varDataPath.toAbsolutePath.toString), tf.constant(varDataTensor))

    val varIdxPath = Paths.get(folder, TensorflowWrapper.VariablesIdxValue)
    val varIdxTensor: TString = tf.io.readFile(tf.constant(varIdxPath.toAbsolutePath.toString)).asTensor
    tf.io.writeFile(tf.constant(varIdxPath.toAbsolutePath.toString), tf.constant(varIdxTensor))

    (varDataPath, varIdxPath, varDataTensor, varIdxTensor)
  }

  private def closeModelTensors(varDataTensor: TString, varIdxTensor: TString) = {
    varDataTensor.close()
    varIdxTensor.close()
  }

  def getSession(configProtoBytes: Option[Array[Byte]] = None): Session = {

    if (m_session == null) {
      val t = new TensorResources()
      val config = configProtoBytes.getOrElse(TensorflowWrapperWithTfIo.TFSessionConfig)

      // save the binary data of variables to file - variables per se
      val path = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + TensorflowWrapperWithTfIo.TFVarsSuffix)
      val folder = path.toAbsolutePath.toString

      val (varDataPath, varIdxPath, varDataTensor, varIdxTensor) = writeModelTensorsToFiles(folder)

      LoadsContrib.loadContribToTensorflow()

      // import the graph
      val _graph = new Graph()
      _graph.importGraphDef(GraphDef.parseFrom(graph))

      // create the session and load the variables
      val session = new Session(_graph, ConfigProto.parseFrom(config))
      val variablesPath = Paths.get(folder, TensorflowWrapperWithTfIo.VariablesKey).toAbsolutePath.toString

      session.runner.addTarget(TensorflowWrapperWithTfIo.SaveRestoreAllOP)
        .feed(TensorflowWrapperWithTfIo.SaveConstOP, t.createTensor(variablesPath))
        .run()

      //delete variable files
      Files.delete(varDataPath)
      Files.delete(varIdxPath)

      // delete tensors manually to avoid memory leaks
      closeModelTensors(varDataTensor, varIdxTensor)

      m_session = session
    }
    m_session
  }

  def getTFHubSession(configProtoBytes: Option[Array[Byte]] = None,
                      initAllTables: Boolean = true,
                      loadSP: Boolean = false,
                      savedSignatures: Option[Map[String, String]] = None): Session = {

    if (m_session == null) {
      val t = new TensorResources()
      val config = configProtoBytes.getOrElse(TensorflowWrapperWithTfIo.TFSessionConfig)

      // save the binary data of variables to file - variables per se
      val path = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + TensorflowWrapperWithTfIo.TFVarsSuffix)
      val folder = path.toAbsolutePath.toString

      val (varDataPath, varIdxPath, varDataTensor, varIdxTensor) = writeModelTensorsToFiles(folder)

      //      val varData = Paths.get(folder, TensorflowWrapperWithTfIo.VariablesPathValue)
      //      Files.write(varData, variables.variables)
      //
      //      // save the binary data of variables to file - variables' index
      //      val varIdx = Paths.get(folder, TensorflowWrapperWithTfIo.VariablesIdxValue)
      //      Files.write(varIdx, variables.index)

      LoadsContrib.loadContribToTensorflow()
      if (loadSP) {
        LoadSentencepiece.loadSPToTensorflowLocally()
        LoadSentencepiece.loadSPToTensorflow()
      }
      // import the graph
      val g = new Graph()
      g.importGraphDef(GraphDef.parseFrom(graph))

      // create the session and load the variables
      val session = new Session(g, ConfigProto.parseFrom(config))
      TensorflowWrapperWithTfIo
        .processInitAllTableOp(initAllTables, t, session, folder, TensorflowWrapperWithTfIo.VariablesKey, savedSignatures = savedSignatures)

      //delete variable files
      Files.delete(varDataPath)
      Files.delete(varIdxPath)

      // delete tensors manually to avoid memory leaks
      closeModelTensors(varDataTensor, varIdxTensor)

      m_session = session
    }
    m_session
  }

  override def createSession(configProtoBytes: Option[Array[Byte]] = None): Session = {

    if (m_session == null) {

      val config = configProtoBytes.getOrElse(TensorflowWrapperWithTfIo.TFSessionConfig)

      LoadsContrib.loadContribToTensorflow()

      // import the graph
      val g = new Graph()
      g.importGraphDef(GraphDef.parseFrom(graph))

      // create the session and load the variables
      val session = new Session(g, ConfigProto.parseFrom(config))

      m_session = session
    }
    m_session
  }

  def saveToFile(file: String, configProtoBytes: Option[Array[Byte]] = None): Unit = {
    val t = new TensorResources()

    // 1. Create tmp director
    val folder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_ner")
      .toAbsolutePath.toString

    val variablesFile = Paths.get(folder, TensorflowWrapperWithTfIo.VariablesKey).toString

    // 2. Save variables
    getSession(configProtoBytes).runner.addTarget(TensorflowWrapperWithTfIo.SaveControlDependenciesOP)
      .feed(TensorflowWrapperWithTfIo.SaveConstOP, t.createTensor(variablesFile))
      .run()

    // 3. Save Graph
    // val graphDef = graph.toGraphDef
    val graphFile = Paths.get(folder, TensorflowWrapperWithTfIo.SavedModelPB).toString
    FileUtils.writeByteArrayToFile(new File(graphFile), graph)

    // 4. Zip folder
    ZipArchiveUtil.zip(folder, file)

    // 5. Remove tmp directory
    FileHelper.delete(folder)
    t.clearTensors()
  }

  /*
  * saveToFileV2 is V2 compatible
  * */
  def saveToFileV1V2(file: String, configProtoBytes: Option[Array[Byte]] = None, savedSignatures: Option[Map[String, String]] = None): Unit = {
    val t = new TensorResources()
    val _tfSignatures: Map[String, String] = savedSignatures.getOrElse(ModelSignatureManager.apply())

    // 1. Create tmp director
    val folder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_ner")
      .toAbsolutePath.toString

    val variablesFile = Paths.get(folder, TensorflowWrapperWithTfIo.VariablesKey).toString

    // 2. Save variables
    def runSessionLegacy = {
      getSession(configProtoBytes).runner.addTarget(TensorflowWrapperWithTfIo.SaveControlDependenciesOP)
        .feed(TensorflowWrapperWithTfIo.SaveConstOP, t.createTensor(variablesFile))
        .run()
    }

    /**
      * addTarget operation is the result of '''saverDef.getSaveTensorName()'''
      * feed operation is the result of '''saverDef.getFilenameTensorName()'''
      *
      * @return List[Tensor]
      */
    def runSessionNew = {
      getSession(configProtoBytes).runner
        .addTarget(_tfSignatures.getOrElse("saveTensorName_", "StatefulPartitionedCall_1"))
        .feed(_tfSignatures.getOrElse("filenameTensorName_", "saver_filename"), t.createTensor(variablesFile))
        .run()
    }

    Try(runSessionLegacy) match {
      case Success(_) => logger.debug("Running legacy session to save variables...")
      case Failure(_) => runSessionNew
    }

    // 3. Save Graph
    val graphFile = Paths.get(folder, TensorflowWrapperWithTfIo.SavedModelPB).toString
    FileUtils.writeByteArrayToFile(new File(graphFile), graph)

    val tfChkPointsVars = FileUtils.listFilesAndDirs(
      new File(folder),
      new WildcardFileFilter("part*"),
      new WildcardFileFilter("variables*")
    ).toArray()

    // TF2 Saved Model generate parts for variables on second save
    // This makes sure they are compatible with V1
    if (tfChkPointsVars.length > 3) {
      val variablesDir = tfChkPointsVars(1).toString

      val (_, _, varDataTensor, varIdxTensor) = writeModelTensorsToFiles(folder)

      FileHelper.delete(variablesDir)
      closeModelTensors(varDataTensor, varIdxTensor)
    }

    // 4. Zip folder
    ZipArchiveUtil.zip(folder, file)

    // 5. Remove tmp directory
    FileHelper.delete(folder)
    t.clearTensors()
  }


  /**
    * Read method to create tmp folder, unpack archive and read file as SavedModelBundle
    *
    * @param file          : the file to read
    * @param zipped        : boolean flag to know if compression is applied
    * @param useBundle     : whether to use the SaveModelBundle object to parse the TF saved model
    * @param tags          : tags to retrieve on the model bundle
    * @param initAllTables : boolean flag whether to retrieve the TF init operation
    * @return Returns a greeting based on the `name` field.
    */
  def read(file: String,
           zipped: Boolean = true,
           useBundle: Boolean = false,
           tags: Array[String] = Array.empty[String],
           initAllTables: Boolean = false,
           savedSignatures: Option[Map[String, String]] = None): (TFWrapper[_], Option[Map[String, String]]) = {

    val t = new TensorResources()

    // 1. Create tmp folder
    val tmpFolder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_ner")
      .toAbsolutePath.toString

    // 2. Unpack archive
    val folder =
      if (zipped)
        ZipArchiveUtil.unzip(new File(file), Some(tmpFolder))
      else
        file

    LoadsContrib.loadContribToTensorflow()

    // 3. Read file as SavedModelBundle
    val (graph, session, varPath, idxPath, signatures) =
      if (useBundle) {
        val model: SavedModelBundle = withSafeSavedModelBundleLoader(tags = tags, savedModelDir = folder)
        val (graph, session, varPath, idxPath) = unpackFromBundle(folder, model)
        if (initAllTables) session.runner().addTarget(InitAllTableOP)

        // Extract saved model signatures
        val saverDef = model.metaGraphDef().getSaverDef
        val signatures = ModelSignatureManager.extractSignatures(model, saverDef)

        (graph, session, varPath, idxPath, signatures)
      } else {
        val (graph, session, varPath, idxPath) = unpackWithoutBundle(folder)
        processInitAllTableOp(initAllTables, t, session, folder, savedSignatures = savedSignatures)

        (graph, session, varPath, idxPath, None)
      }

    val tf = Ops.create
    val varTensor: TString = tf.io.readFile(tf.constant(varPath.toAbsolutePath.toString)).asTensor
    val idxTensor: TString = tf.io.readFile(tf.constant(idxPath.toAbsolutePath.toString)).asTensor

    // 4. Remove tmp folder
    FileHelper.delete(tmpFolder)
    t.clearTensors()

    // Important to avoid mem leaks
    varTensor.close()
    idxTensor.close()

    val tfWrapper =
      new TensorflowWrapperWithTfIo(
        VariablesTfIo(varTensor, idxTensor), graph.toGraphDef.toByteArray)

    tfWrapper.m_session = session
    (tfWrapper, signatures)
  }

  override def getGraph(): Array[Byte] = graph
}

/** Companion object */
object TensorflowWrapperWithTfIo {
  private[TensorflowWrapperWithTfIo] val logger: Logger = LoggerFactory.getLogger("TensorflowWrapperWithTfIo")

  /** log_device_placement=True, allow_soft_placement=True, gpu_options.allow_growth=True */
  private final val TFSessionConfig: Array[Byte] = Array[Byte](50, 2, 32, 1, 56, 1)

  // Variables
  val VariablesKey = "variables"
  val VariablesPathValue = "variables.data-00000-of-00001"
  val VariablesIdxValue = "variables.index"

  // Operations
  val InitAllTableOP = "init_all_tables"
  val SaveRestoreAllOP = "save/restore_all"
  val SaveConstOP = "save/Const"
  val SaveControlDependenciesOP = "save/control_dependency"

  // Model
  val SavedModelPB = "saved_model.pb"

  // TF vars suffix folder
  val TFVarsSuffix = "_tf_vars"

  /** Utility method to load the TF saved model bundle */
  def withSafeSavedModelBundleLoader(tags: Array[String], savedModelDir: String): SavedModelBundle = {
    Try(SavedModelBundle.load(savedModelDir, tags: _*)) match {
      case Success(bundle) => bundle
      case Failure(s) => throw new Exception(s"Could not retrieve the SavedModelBundle + ${s.printStackTrace()}")
    }
  }

  /** Utility method to load the TF saved model components without a provided bundle */
  private def unpackWithoutBundle(folder: String) = {
    val graph = readGraph(Paths.get(folder, SavedModelPB).toString)
    val session = new Session(graph, ConfigProto.parseFrom(TFSessionConfig))
    val varPath = Paths.get(folder, VariablesPathValue)
    val idxPath = Paths.get(folder, VariablesIdxValue)
    (graph, session, varPath, idxPath)
  }

  /** Utility method to load the TF saved model components from a provided bundle */
  private def unpackFromBundle(folder: String, model: SavedModelBundle) = {
    val graph = model.graph()
    val session = model.session()
    val varPath = Paths.get(folder, VariablesKey, VariablesPathValue)
    val idxPath = Paths.get(folder, VariablesKey, VariablesIdxValue)
    (graph, session, varPath, idxPath)
  }

  /** Utility method to process init all table operation key */
  private def processInitAllTableOp(initAllTables: Boolean,
                                    tensorResources: TensorResources,
                                    session: Session,
                                    variablesDir: String,
                                    variablesKey: String = VariablesKey,
                                    savedSignatures: Option[Map[String, String]] = None) = {


    val _tfSignatures: Map[String, String] = savedSignatures.getOrElse(ModelSignatureManager.apply())

    lazy val legacySessionRunner = session.runner
      .addTarget(SaveRestoreAllOP)
      .feed(SaveConstOP, tensorResources.createTensor(Paths.get(variablesDir, variablesKey).toString))

    /**
      * addTarget operation is the result of '''saverDef.getRestoreOpName()'''
      * feed operation is the result of '''saverDef.getFilenameTensorName()'''
      */
    lazy val newSessionRunner = session.runner
      .addTarget(_tfSignatures.getOrElse("restoreOpName_", "StatefulPartitionedCall_2"))
      .feed(_tfSignatures.getOrElse("filenameTensorName_", "saver_filename"), tensorResources.createTensor(Paths.get(variablesDir, variablesKey).toString))

    def runRestoreNewNoInit = {
      newSessionRunner.run()
    }

    def runRestoreNewInit = {
      newSessionRunner.addTarget(InitAllTableOP).run()
    }

    def runRestoreLegacyNoInit = {
      legacySessionRunner.run()
    }

    def runRestoreLegacyInit = {
      legacySessionRunner.addTarget(InitAllTableOP).run()
    }

    if (initAllTables) {
      Try(runRestoreLegacyInit) match {
        case Success(_) => logger.debug("Running restore legacy with init...")
        case Failure(_) => runRestoreNewInit
      }
    } else {
      Try(runRestoreLegacyNoInit) match {
        case Success(_) => logger.debug("Running restore legacy with no init...")
        case Failure(_) => runRestoreNewNoInit
      }
    }
  }

  /** Utility method to load a Graph from path */
  def readGraph(graphFile: String): Graph = {
    val graphBytesDef = FileUtils.readFileToByteArray(new File(graphFile))
    val graph = new Graph()
    try {
      graph.importGraphDef(GraphDef.parseFrom(graphBytesDef))
    } catch {
      case e: TensorFlowException if e.getMessage.contains("Op type not registered 'BlockLSTM'") =>
        throw new UnsupportedOperationException("Spark NLP tried to load a TensorFlow Graph using Contrib module, but" +
          " failed to load it on this system. If you are on Windows, please follow the correct steps for setup: " +
          "https://github.com/JohnSnowLabs/spark-nlp/issues/1022" +
          s" If not the case, please report this issue. Original error message:\n\n${e.getMessage}")
    }
    graph
  }

  /**
    * Read method to create tmp folder, unpack archive and read file as SavedModelBundle
    *
    * @param file          : the file to read
    * @param zipped        : boolean flag to know if compression is applied
    * @param useBundle     : whether to use the SaveModelBundle object to parse the TF saved model
    * @param tags          : tags to retrieve on the model bundle
    * @param initAllTables : boolean flag whether to retrieve the TF init operation
    * @return Returns a greeting based on the `name` field.
    */
  def read(file: String,
           zipped: Boolean = true,
           useBundle: Boolean = false,
           tags: Array[String] = Array.empty[String],
           initAllTables: Boolean = false,
           savedSignatures: Option[Map[String, String]] = None): (TensorflowWrapperWithTfIo, Option[Map[String, String]]) = {

    val t = new TensorResources()

    // 1. Create tmp folder
    val tmpFolder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_ner")
      .toAbsolutePath.toString

    // 2. Unpack archive
    val folder =
      if (zipped)
        ZipArchiveUtil.unzip(new File(file), Some(tmpFolder))
      else
        file

    LoadsContrib.loadContribToTensorflow()

    // 3. Read file as SavedModelBundle
    val (graph, session, varPath, idxPath, signatures) =
      if (useBundle) {
        val model: SavedModelBundle = withSafeSavedModelBundleLoader(tags = tags, savedModelDir = folder)
        val (graph, session, varPath, idxPath) = unpackFromBundle(folder, model)
        if (initAllTables) session.runner().addTarget(InitAllTableOP)

        // Extract saved model signatures
        val saverDef = model.metaGraphDef().getSaverDef
        val signatures = ModelSignatureManager.extractSignatures(model, saverDef)

        (graph, session, varPath, idxPath, signatures)
      } else {
        val (graph, session, varPath, idxPath) = unpackWithoutBundle(folder)
        processInitAllTableOp(initAllTables, t, session, folder, savedSignatures = savedSignatures)

        (graph, session, varPath, idxPath, None)
      }

    println("Using TF IO!")
    println(s"varPath: $varPath ||| idxPath: $idxPath")

    val tf = Ops.create
    val varTensor: TString = tf.io.readFile(tf.constant(varPath.toAbsolutePath.toString)).asTensor
    val idxTensor: TString = tf.io.readFile(tf.constant(idxPath.toAbsolutePath.toString)).asTensor

    val tfWrapper =
      new TensorflowWrapperWithTfIo(
        VariablesTfIo(varTensor, idxTensor), graph.toGraphDef.toByteArray)

    // 4. Remove tmp folder
    FileHelper.delete(tmpFolder)
    t.clearTensors()

    // Important to avoid mem leaks
    varTensor.close()
    idxTensor.close()

    tfWrapper.m_session = session
    (tfWrapper, signatures)
  }

  def readWithSP(
                  file: String,
                  zipped: Boolean = true,
                  useBundle: Boolean = false,
                  tags: Array[String] = Array.empty[String],
                  initAllTables: Boolean = false,
                  loadSP: Boolean = false
                ): TensorflowWrapperWithTfIo = {
    val t = new TensorResources()

    // 1. Create tmp folder
    val tmpFolder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_ner")
      .toAbsolutePath.toString

    // 2. Unpack archive
    val folder = if (zipped)
      ZipArchiveUtil.unzip(new File(file), Some(tmpFolder))
    else
      file

    if (loadSP) {
      LoadSentencepiece.loadSPToTensorflowLocally()
      LoadSentencepiece.loadSPToTensorflow()
    }
    // 3. Read file as SavedModelBundle
    val (graph, session, varPath, idxPath) =
      if (useBundle) {
        val model: SavedModelBundle = withSafeSavedModelBundleLoader(tags = tags, savedModelDir = folder)
        val (graph, session, varPath, idxPath) = unpackFromBundle(folder, model)
        if (initAllTables) session.runner().addTarget(InitAllTableOP)
        (graph, session, varPath, idxPath)
      } else {
        val (graph, session, varPath, idxPath) = unpackWithoutBundle(folder)
        processInitAllTableOp(initAllTables, t, session, folder)
        (graph, session, varPath, idxPath)
      }

    // 4. Remove tmp folder
    FileHelper.delete(tmpFolder)
    t.clearTensors()

    val tf = Ops.create
    val varTensor: TString = tf.io.readFile(tf.constant(varPath.toAbsolutePath.toString)).asTensor
    val idxTensor: TString = tf.io.readFile(tf.constant(idxPath.toAbsolutePath.toString)).asTensor

    val tfWrapper =
      new TensorflowWrapperWithTfIo(
        VariablesTfIo(varTensor, idxTensor), graph.toGraphDef.toByteArray)

    // Important to avoid mem leaks
    varTensor.close()
    idxTensor.close()

    tfWrapper.m_session = session
    tfWrapper
  }

  def readZippedSavedModel(
                            rootDir: String = "",
                            fileName: String = "",
                            tags: Array[String] = Array.empty[String],
                            initAllTables: Boolean = false
                          ): TensorflowWrapperWithTfIo = {
    val tensorResources = new TensorResources()

    val listFiles = ResourceHelper.listResourceDirectory(rootDir)

    val path =
      if (listFiles.length > 1)
        s"${listFiles.head.split("/").head}/$fileName"
      else
        listFiles.head

    val uri = new URI(path.replaceAllLiterally("\\", "/"))

    val inputStream = ResourceHelper.getResourceStream(uri.toString)

    // 1. Create tmp folder
    val tmpFolder =
      Files
        .createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_classifier_dl_zip")
        .toAbsolutePath.toString

    val zipFile = new File(tmpFolder, "tmp_classifier_dl.zip")

    Files.copy(inputStream, zipFile.toPath)

    // 2. Unpack archive
    val folder = ZipArchiveUtil.unzip(zipFile, Some(tmpFolder))

    // 3. Create second tmp folder
    val finalFolder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_classifier_dl")
      .toAbsolutePath.toString

    val variablesFile = Paths.get(finalFolder, VariablesKey).toAbsolutePath
    Files.createDirectory(variablesFile)

    // 4. Copy the saved_model.zip into tmp folder
    val savedModelInputStream = ResourceHelper.getResourceStream(new Path(folder, SavedModelPB).toString)
    val savedModelFile = new File(finalFolder, SavedModelPB)
    Files.copy(savedModelInputStream, savedModelFile.toPath)

    val varIndexInputStream = ResourceHelper.getResourceStream(new Path(folder, VariablesIdxValue).toString)
    val varIndexFile = new File(variablesFile.toString, VariablesIdxValue)
    Files.copy(varIndexInputStream, varIndexFile.toPath)

    val varDataInputStream = ResourceHelper.getResourceStream(new Path(folder, VariablesPathValue).toString)
    val varDataFile = new File(variablesFile.toString, VariablesPathValue)
    Files.copy(varDataInputStream, varDataFile.toPath)

    // 5. Read file as SavedModelBundle
    val model = withSafeSavedModelBundleLoader(tags = tags, savedModelDir = finalFolder)

    val (graph, session, varPath, idxPath) = unpackFromBundle(finalFolder, model)

    if (initAllTables) session.runner().addTarget(InitAllTableOP)

    val tf = Ops.create
    val varTensor: TString = tf.io.readFile(tf.constant(varPath.toAbsolutePath.toString)).asTensor
    val idxTensor: TString = tf.io.readFile(tf.constant(idxPath.toAbsolutePath.toString)).asTensor

    val tfWrapper =
      new TensorflowWrapperWithTfIo(
        VariablesTfIo(varTensor, idxTensor),
        graph.toGraphDef.toByteArray)

    // 6. Remove tmp folder
    FileHelper.delete(tmpFolder)
    FileHelper.delete(finalFolder)
    FileHelper.delete(folder)
    tensorResources.clearTensors()

    // Important to avoid mem leaks
    varTensor.close()
    idxTensor.close()

    tfWrapper.m_session = session
    tfWrapper
  }

  def readChkPoints(
                     file: String,
                     zipped: Boolean = true,
                     tags: Array[String] = Array.empty[String],
                     initAllTables: Boolean = false
                   ): TensorflowWrapperWithTfIo = {
    val t = new TensorResources()

    // 1. Create tmp folder
    val tmpFolder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + "_ner")
      .toAbsolutePath.toString

    // 2. Unpack archive
    val folder =
      if (zipped)
        ZipArchiveUtil.unzip(new File(file), Some(tmpFolder))
      else
        file

    LoadsContrib.loadContribToTensorflow()

    val tfChkPointsVars = FileUtils.listFilesAndDirs(
      new File(folder),
      new WildcardFileFilter("part*"),
      new WildcardFileFilter("variables*")
    ).toArray()

    val variablesDir = tfChkPointsVars(1).toString
    val variablesData = tfChkPointsVars(2).toString
    val variablesIndex = tfChkPointsVars(3).toString

    // 3. Read file as SavedModelBundle
    val graph = readGraph(Paths.get(folder, SavedModelPB).toString)
    val session = new Session(graph, ConfigProto.parseFrom(TFSessionConfig))
    val varPath = Paths.get(variablesData)
    val idxPath = Paths.get(variablesIndex)

    processInitAllTableOp(initAllTables, t, session, variablesDir, variablesKey = "part-00000-of-00001")

    //    val varBytes = Files.readAllBytes(varPath)
    //    val idxBytes = Files.readAllBytes(idxPath)

    val tf = Ops.create
    val varTensor: TString = tf.io.readFile(tf.constant(varPath.toAbsolutePath.toString)).asTensor
    val idxTensor: TString = tf.io.readFile(tf.constant(idxPath.toAbsolutePath.toString)).asTensor

    val tfWrapper =
      new TensorflowWrapperWithTfIo(
        VariablesTfIo(varTensor, idxTensor),
        graph.toGraphDef.toByteArray)

    // 4. Remove tmp folder
    FileHelper.delete(tmpFolder)
    t.clearTensors()
    // Important to avoid mem leaks
    varTensor.close()
    idxTensor.close()

    tfWrapper.m_session = session
    tfWrapper
  }

  def extractVariablesSavedModel(session: Session): Variables = {
    val t = new TensorResources()

    val folder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + TFVarsSuffix)
      .toAbsolutePath.toString
    val variablesFile = Paths.get(folder, VariablesKey).toString

    session.runner.addTarget(SaveControlDependenciesOP)
      .feed(SaveConstOP, t.createTensor(variablesFile))
      .run()

    val varPath = Paths.get(folder, VariablesPathValue)
    val varBytes = Files.readAllBytes(varPath)

    val idxPath = Paths.get(folder, VariablesIdxValue)
    val idxBytes = Files.readAllBytes(idxPath)

    val vars = Variables(varBytes, idxBytes)

    FileHelper.delete(folder)

    vars
  }

}
