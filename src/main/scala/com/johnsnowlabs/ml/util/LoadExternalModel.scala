/*
 * Copyright 2017-2022 John Snow Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.johnsnowlabs.ml.util

import com.johnsnowlabs.ml.tensorflow.sentencepiece.SentencePieceWrapper
import com.johnsnowlabs.nlp.util.io.{ExternalResource, ReadAs, ResourceHelper}

import java.io.File
import java.nio.file.Paths
import scala.io.Source

object LoadExternalModel {

  val notSupportedEngineError: String = {
    s"""Your imported model is either not supported or not correctly imported.
       |
       |A typical imported TensorFlow SavedModel has the following structure:
       |
       |├── assets/
       |    ├── your-assets-are-here (vocab, sp model, labels, etc.)
       |├── saved_model.pb
       |└── variables/
       |    ├── variables.data-00000-of-00001
       |    └── variables.index
       |
       |Please make sure you follow provided notebooks to import external models into Spark NLP:
       |https://github.com/JohnSnowLabs/spark-nlp/discussions/5669""".stripMargin
  }

  def isTensorFlowModel(modelPath: String): Boolean = {
    val tfSavedModel = new File(modelPath, ModelEngine.tensorflowModelName)
    tfSavedModel.exists()

  }

  def isOnnxModel(modelPath: String, isEncoderDecoder: Boolean = false): Boolean = {

    if (isEncoderDecoder) {
      val onnxEncoderModel = new File(modelPath, ModelEngine.onnxEncoderModel)
      val onnxDecoderModel = new File(modelPath, ModelEngine.onnxDecoderModel)
      onnxEncoderModel.exists() && onnxDecoderModel.exists()
    } else {
      val onnxModel = new File(modelPath, ModelEngine.onnxModelName)
      onnxModel.exists()
    }

  }

  def detectEngine(modelPath: String, isEncoderDecoder: Boolean = false): String = {

    /** Check if the path is correct */
    val f = new File(modelPath)
    require(f.exists, s"Folder $modelPath not found")
    require(f.isDirectory, s"Folder $modelPath is not folder")

    /*Check if the assets path is correct*/
    val assetsPath = Paths.get(modelPath, "/assets").toString
    val assetsPathFile = new File(assetsPath)
    require(assetsPathFile.exists, s"Folder $assetsPath not found")
    require(assetsPathFile.isDirectory, s"Folder $assetsPath is not folder")

    /*TensorFlow required model's name*/
    val tfSavedModelExist = isTensorFlowModel(modelPath)

    /*ONNX required model's name*/
    val onnxModelExist = isOnnxModel(modelPath, isEncoderDecoder)

    if (tfSavedModelExist) {
      ModelEngine.tensorflow
    } else if (onnxModelExist) {
      ModelEngine.onnx
    } else {
      require(tfSavedModelExist || onnxModelExist, notSupportedEngineError)
      ModelEngine.unk
    }

  }

  /** Retrieves a local path for a model folder and detects DL engine
    *
    * If the model is at a remote location it will be downloaded and a local path provided.
    * Otherwise an URL to the local path of the folder will be returned. This method also tests
    * the sanity of the DL model while detecting the DL engine
    *
    * @param path
    *   Local or Remote path of the model folder
    * @return
    *   URL to the local path of the folder
    */
  def modelSanityCheck(path: String, isEncoderDecoder: Boolean = false): (String, String) = {
    val localPath: String = ResourceHelper.copyToLocal(path)

    (localPath, detectEngine(localPath, isEncoderDecoder))
  }

  def loadTextAsset(assetPath: String, assetName: String): Array[String] = {
    val assetFile = checkAndCreateFile(assetPath + "/assets", assetName)

    // Convert to URL first to access correct file protocol
    val assetResource =
      new ExternalResource(assetFile.toURI.toURL.toString, ReadAs.TEXT, Map("format" -> "text"))
    ResourceHelper.parseLines(assetResource)
  }

  /** @param assetPath
    *   path to root of assets directory
    * @param assetName
    *   asset's name
    * @return
    *   SentencePieceWrapper
    */
  def loadSentencePieceAsset(assetPath: String, assetName: String): SentencePieceWrapper = {
    val assetFile = checkAndCreateFile(assetPath + "/assets", assetName)
    SentencePieceWrapper.read(assetFile.toString)
  }

  /** @param assetPath
    *   path to root of assets directory
    * @param assetName
    *   asset's name
    * @return
    *   JSON as String to be parsed later
    */
  def loadJsonStringAsset(assetPath: String, assetName: String): String = {
    val assetFile = checkAndCreateFile(assetPath + "/assets", assetName)
    val vocabStream = ResourceHelper.getResourceStream(assetFile.getAbsolutePath)
    Source.fromInputStream(vocabStream).mkString
  }

  /** @param filePath
    *   path to the file
    * @param fileName
    *   file's name
    * @return
    *   File if the file exists
    */
  private def checkAndCreateFile(filePath: String, fileName: String): File = {
    val f = new File(filePath, fileName)
    require(f.exists(), s"File $fileName not found in folder $filePath")
    f
  }

}
