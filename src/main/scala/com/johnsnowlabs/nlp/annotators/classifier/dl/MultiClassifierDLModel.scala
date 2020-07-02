package com.johnsnowlabs.nlp.annotators.classifier.dl

import com.johnsnowlabs.ml.tensorflow.{ClassifierDatasetEncoder, ClassifierDatasetEncoderParams, ReadTensorflowModel, TensorflowMultiClassifier, TensorflowWrapper, WriteTensorflowModel}
import com.johnsnowlabs.nlp.AnnotatorType.{CATEGORY, WORD_EMBEDDINGS}
import com.johnsnowlabs.nlp._
import com.johnsnowlabs.nlp.annotators.ner.Verbose
import com.johnsnowlabs.nlp.pretrained.ResourceDownloader
import com.johnsnowlabs.nlp.serialization.StructFeature
import com.johnsnowlabs.storage.HasStorageRef
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.param.{FloatParam, IntArrayParam}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.{Dataset, SparkSession}


/**
  * ClassifierDL is a generic Multi-class Text Classification. ClassifierDL uses the state-of-the-art Universal Sentence Encoder as an input for text classifications. The ClassifierDL annotator uses a deep learning model (DNNs) we have built inside TensorFlow and supports up to 50 classes
  *
  * NOTE: This annotator accepts a label column of a single item in either type of String, Int, Float, or Double.
  *
  * NOTE: UniversalSentenceEncoder and SentenceEmbeddings can be used for the inputCol
  *
  * See [[https://github.com/JohnSnowLabs/spark-nlp/blob/master/src/test/scala/com/johnsnowlabs/nlp/annotators/classifier/dl/ClassifierDLTestSpec.scala]] for further reference on how to use this API
  *
  * @groupname anno Annotator types
  * @groupdesc anno Required input and expected output annotator types
  * @groupname Ungrouped Members
  * @groupname param Parameters
  * @groupname setParam Parameter setters
  * @groupname getParam Parameter getters
  * @groupname Ungrouped Members
  * @groupprio param  1
  * @groupprio anno  2
  * @groupprio Ungrouped 3
  * @groupprio setParam  4
  * @groupprio getParam  5
  * @groupdesc Parameters A list of (hyper-)parameter keys this annotator can take. Users can set and get the parameter values through setters and getters, respectively.
  *
  **/
class MultiClassifierDLModel(override val uid: String)
  extends AnnotatorModel[MultiClassifierDLModel]
    with WriteTensorflowModel
    with HasStorageRef
    with ParamsAndFeaturesWritable {
  def this() = this(Identifiable.randomUID("MultiClassifierDLModel"))

  /** Output annotator type : SENTENCE_EMBEDDINGS
    *
    * @group anno
    **/
  override val inputAnnotatorTypes: Array[AnnotatorType] = Array(WORD_EMBEDDINGS)
  /** Output annotator type : CATEGORY
    *
    * @group anno
    **/
  override val outputAnnotatorType: String = CATEGORY

  /** ConfigProto from tensorflow, serialized into byte array. Get with config_proto.SerializeToString()
    *
    * @group param
    **/
  val configProtoBytes = new IntArrayParam(this, "configProtoBytes", "ConfigProto from tensorflow, serialized into byte array. Get with config_proto.SerializeToString()")

  /** The minimum threshold for each label to be accepted. Default is 0.5
    *
    * @group param
    **/
  val threshold = new FloatParam(this, "threshold", "The minimum threshold for each label to be accepted. Default is 0.5")

  /** Tensorflow config Protobytes passed to the TF session
    *
    * @group setParam
    **/
  def setConfigProtoBytes(
                           bytes: Array[Int]
                         ): MultiClassifierDLModel.this.type = set(this.configProtoBytes, bytes)

  /** Tensorflow config Protobytes passed to the TF session
    *
    * @group getParam
    **/
  def getConfigProtoBytes: Option[Array[Byte]] =
    get(this.configProtoBytes).map(_.map(_.toByte))

  /**
    * datasetParams
    *
    * @group param */
  val datasetParams = new StructFeature[ClassifierDatasetEncoderParams](this, "datasetParams")

  /**
    * datasetParams
    *
    * @group setParam */
  def setDatasetParams(params: ClassifierDatasetEncoderParams): MultiClassifierDLModel.this.type =
    set(this.datasetParams, params)

  /** The minimum threshold for each label to be accepted. Default is 0.5
    *
    * @group setParam
    **/
  def setThreshold(threshold: Float): MultiClassifierDLModel.this.type = set(this.threshold, threshold)

  /** @group param */
  private var _model: Option[Broadcast[TensorflowMultiClassifier]] = None

  /** @group setParam */
  def setModelIfNotSet(spark: SparkSession, tf: TensorflowWrapper): this.type = {
    if (_model.isEmpty) {

      require(datasetParams.isSet, "datasetParams must be set before usage")

      val encoder = new ClassifierDatasetEncoder(datasetParams.get.get)

      _model = Some(
        spark.sparkContext.broadcast(
          new TensorflowMultiClassifier(
            tf,
            encoder,
            Verbose.Silent
          )
        )
      )
    }
    this
  }

  /** @group getParam */
  def getModelIfNotSet: TensorflowMultiClassifier = _model.get.value

  /** The minimum threshold for each label to be accepted. Default is 0.5
    *
    * @group getParam
    **/
  def getThreshold: Float = $(this.threshold)

  setDefault(
    threshold -> 0.5f
  )

  override protected def beforeAnnotate(dataset: Dataset[_]): Dataset[_] = {
    validateStorageRef(dataset, $(inputCols), AnnotatorType.WORD_EMBEDDINGS)
    dataset
  }

  /**
    * takes a document and annotations and produces new annotations of this annotator's annotation type
    *
    * @param annotations Annotations that correspond to inputAnnotationCols generated by previous annotators if any
    * @return any number of annotations processed for every input annotation. Not necessary one to one relationship
    */
  override def annotate(annotations: Seq[Annotation]): Seq[Annotation] = {
    val sentences = annotations
      .filter(_.annotatorType == WORD_EMBEDDINGS)
      .groupBy(_.metadata.getOrElse[String]("sentence", "0").toInt)
      .toSeq
      .sortBy(_._1)

    if(sentences.nonEmpty) {
      getModelIfNotSet.predict(sentences, $(threshold), getConfigProtoBytes)
    }else {
      Seq.empty[Annotation]
    }
  }

  override def onWrite(path: String, spark: SparkSession): Unit = {
    super.onWrite(path, spark)
    writeTensorflowModel(
      path,
      spark,
      getModelIfNotSet.tensorflow,
      "_classifierdl",
      MultiClassifierDLModel.tfFile,
      configProtoBytes = getConfigProtoBytes
    )

  }
}

trait ReadablePretrainedMultiClassifierDL
  extends ParamsAndFeaturesReadable[MultiClassifierDLModel]
    with HasPretrained[MultiClassifierDLModel] {
  override val defaultModelName: Some[String] = Some("classifierdl_use_trec6")

  override def pretrained(name: String, lang: String, remoteLoc: String): MultiClassifierDLModel = {
    ResourceDownloader.downloadModel(MultiClassifierDLModel, name, Option(lang), remoteLoc)
  }

  /** Java compliant-overrides */
  override def pretrained(): MultiClassifierDLModel = pretrained(defaultModelName.get, defaultLang, defaultLoc)
  override def pretrained(name: String): MultiClassifierDLModel = pretrained(name, defaultLang, defaultLoc)
  override def pretrained(name: String, lang: String): MultiClassifierDLModel = pretrained(name, lang, defaultLoc)
}

trait ReadMultiClassifierDLTensorflowModel extends ReadTensorflowModel {
  this: ParamsAndFeaturesReadable[MultiClassifierDLModel] =>

  override val tfFile: String = "classifierdl_tensorflow"

  def readTensorflow(instance: MultiClassifierDLModel, path: String, spark: SparkSession): Unit = {

    val tf = readTensorflowChkPoints(path, spark, "_classifierdl_tf", initAllTables = true)
    instance.setModelIfNotSet(spark, tf)
  }

  addReader(readTensorflow)
}

object MultiClassifierDLModel extends ReadablePretrainedMultiClassifierDL with ReadMultiClassifierDLTensorflowModel
