package com.johnsnowlabs.nlp.embeddings

import java.io.File

import com.johnsnowlabs.ml.tensorflow.{ReadTensorflowModel, TensorflowUSE, TensorflowWrapper, WriteTensorflowModel}
import com.johnsnowlabs.nlp.AnnotatorType.{DOCUMENT, SENTENCE_EMBEDDINGS}
import com.johnsnowlabs.nlp.annotators.common.SentenceSplit
import com.johnsnowlabs.nlp.{Annotation, AnnotatorModel, HasPretrained, ParamsAndFeaturesReadable}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.param.{IntArrayParam, Param}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.SparkSession

class UniversalSentenceEncoder(override val uid: String)
  extends AnnotatorModel[UniversalSentenceEncoder]
    with WriteTensorflowModel
    with HasEmbeddings {

  /** Annotator reference id. Used to identify elements in metadata or to refer to this annotator type */
  def this() = this(Identifiable.randomUID("UNIVERSAL_SENTENCE_ENCODER"))

  override val outputAnnotatorType: AnnotatorType = SENTENCE_EMBEDDINGS

  override val inputAnnotatorTypes: Array[AnnotatorType] = Array(DOCUMENT)

  val tfHubModelPath =
    new Param[String](this, "tfHubModelPath", "Internal use only.")

  def setTfHubModelPath(path: String): UniversalSentenceEncoder.this.type =
    set(this.tfHubModelPath, path)

  def getTfHubModelPath: String = $(tfHubModelPath)

  val configProtoBytes = new IntArrayParam(
    this,
    "configProtoBytes",
    "ConfigProto from tensorflow, serialized into byte array. Get with config_proto.SerializeToString()"
  )

  def setConfigProtoBytes(
                           bytes: Array[Int]
                         ): UniversalSentenceEncoder.this.type = set(this.configProtoBytes, bytes)

  def getConfigProtoBytes: Option[Array[Byte]] =
    get(this.configProtoBytes).map(_.map(_.toByte))

  private var _model: Option[Broadcast[TensorflowUSE]] = None

  def getModelIfNotSet: TensorflowUSE = _model.get.value

  def setModelIfNotSet(spark: SparkSession,
                       tensorflow: TensorflowWrapper): this.type = {
    if (_model.isEmpty) {

      _model = Some(
        spark.sparkContext.broadcast(
          new TensorflowUSE(tensorflow, configProtoBytes = getConfigProtoBytes)
        )
      )
    }
    this
  }

  /**
    * takes a document and annotations and produces new annotations of this annotator's annotation type
    *
    * @param annotations Annotations that correspond to inputAnnotationCols generated by previous annotators if any
    * @return any number of annotations processed for every input annotation. Not necessary one to one relationship
    */
  override def annotate(annotations: Seq[Annotation]): Seq[Annotation] = {
    val sentences = SentenceSplit.unpack(annotations)
    getModelIfNotSet.calculateEmbeddings(sentences)
  }

  override def onWrite(path: String, spark: SparkSession): Unit = {
    super.onWrite(path, spark)
    writeTensorflowHub(path, tfPath = getTfHubModelPath, spark)
  }

}

trait ReadablePretrainedUSEModel
  extends ParamsAndFeaturesReadable[UniversalSentenceEncoder]
    with HasPretrained[UniversalSentenceEncoder] {
  override val defaultModelName: Some[String] = Some("tfhub_use")

  /** Java compliant-overrides */
  override def pretrained(): UniversalSentenceEncoder = super.pretrained()

  override def pretrained(name: String): UniversalSentenceEncoder = super.pretrained(name)

  override def pretrained(name: String, lang: String): UniversalSentenceEncoder = super.pretrained(name, lang)

  override def pretrained(name: String, lang: String, remoteLoc: String): UniversalSentenceEncoder = super.pretrained(name, lang, remoteLoc)
}

trait ReadUSETensorflowModel extends ReadTensorflowModel {
  this: ParamsAndFeaturesReadable[UniversalSentenceEncoder] =>

  /*Needs to point to an actual folder rather than a .pb file*/
  override val tfFile: String = ""

  def readTensorflow(instance: UniversalSentenceEncoder,
                     path: String,
                     spark: SparkSession): Unit = {
    val tf = readTensorflowHub(
      path,
      spark,
      "_use_tf",
      zipped = false,
      useBundle = true,
      tags = Array("serve")
    )
    instance.setModelIfNotSet(spark, tf)
  }

  addReader(readTensorflow)

  def loadSavedModel(tfHubPath: String,
                     spark: SparkSession): UniversalSentenceEncoder = {
    val f = new File(tfHubPath)
    val savedModel = new File(tfHubPath, "saved_model.pb")

    require(f.exists, s"Folder $tfHubPath not found")
    require(f.isDirectory, s"File $tfHubPath is not folder")
    require(
      savedModel.exists(),
      s"savedModel file saved_model.pb not found in folder $tfHubPath"
    )

    new UniversalSentenceEncoder()
      .setTfHubModelPath(tfHubPath)
  }
}

object UniversalSentenceEncoder
  extends ReadablePretrainedUSEModel
    with ReadUSETensorflowModel
