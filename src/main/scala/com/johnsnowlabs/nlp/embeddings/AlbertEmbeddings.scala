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

package com.johnsnowlabs.nlp.embeddings

import com.johnsnowlabs.ml.pytorch.{PytorchAlbert, PytorchWrapper, ReadPytorchModel, WritePytorchModel}
import com.johnsnowlabs.ml.tensorflow._
import com.johnsnowlabs.ml.tensorflow.sentencepiece._
import com.johnsnowlabs.nlp._
import com.johnsnowlabs.nlp.annotators.common._
import com.johnsnowlabs.storage.HasStorageRef
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.param.{IntParam, Param}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.io.File

/** ALBERT: A LITE BERT FOR SELF-SUPERVISED LEARNING OF LANGUAGE REPRESENTATIONS - Google Research, Toyota Technological Institute at Chicago
 *
 * These word embeddings represent the outputs generated by the Albert model.
 * All official Albert releases by google in TF-HUB are supported with this Albert Wrapper:
 *
 * '''Ported TF-Hub Models:'''
 *
 * `"albert_base_uncased"`    | [[https://tfhub.dev/google/albert_base/3 albert_base]]       |  768-embed-dim,   12-layer,  12-heads, 12M parameters
 *
 * `"albert_large_uncased"`   | [[https://tfhub.dev/google/albert_large/3 albert_large]]     |  1024-embed-dim,  24-layer,  16-heads, 18M parameters
 *
 * `"albert_xlarge_uncased"`  | [[https://tfhub.dev/google/albert_xlarge/3 albert_xlarge]]   |  2048-embed-dim,  24-layer,  32-heads, 60M parameters
 *
 * `"albert_xxlarge_uncased"` | [[https://tfhub.dev/google/albert_xxlarge/3 albert_xxlarge]] |  4096-embed-dim,  12-layer,  64-heads, 235M parameters
 *
 * This model requires input tokenization with SentencePiece model, which is provided by Spark-NLP (See tokenizers package).
 *
 * Pretrained models can be loaded with `pretrained` of the companion object:
 * {{{
 * val embeddings = AlbertEmbeddings.pretrained()
 *  .setInputCols("sentence", "token")
 *  .setOutputCol("embeddings")
 * }}}
 * The default model is `"albert_base_uncased"`, if no name is provided.
 *
 * For extended examples of usage, see the [[https://github.com/JohnSnowLabs/spark-nlp-workshop/blob/master/jupyter/training/english/dl-ner/ner_albert.ipynb Spark NLP Workshop]]
 * and the [[https://github.com/JohnSnowLabs/spark-nlp/blob/master/src/test/scala/com/johnsnowlabs/nlp/embeddings/AlbertEmbeddingsTestSpec.scala AlbertEmbeddingsTestSpec]].
 * Models from the HuggingFace 🤗 Transformers library are also compatible with Spark NLP 🚀. The Spark NLP Workshop
 * example shows how to import them [[https://github.com/JohnSnowLabs/spark-nlp/discussions/5669]].
 *
 * '''Sources:'''
 *
 * [[https://arxiv.org/pdf/1909.11942.pdf ALBERT: A LITE BERT FOR SELF-SUPERVISED LEARNING OF LANGUAGE REPRESENTATIONS]]
 *
 * [[https://github.com/google-research/ALBERT]]
 *
 * [[https://tfhub.dev/s?q=albert]]
 *
 * '''Paper abstract:'''
 *
 * ''Increasing model size when pretraining natural language representations often results in improved performance on
 * downstream tasks. However, at some point further model increases become harder due to GPU/TPU memory limitations and
 * longer training times. To address these problems, we present two parameter reduction techniques to lower memory
 * consumption and increase the training speed of BERT (Devlin et al., 2019). Comprehensive empirical evidence shows
 * that our proposed methods lead to models that scale much better compared to
 * the original BERT. We also use a self-supervised loss that focuses on modeling
 * inter-sentence coherence, and show it consistently helps downstream tasks with
 * multi-sentence inputs. As a result, our best model establishes new state-of-the-art
 * results on the GLUE, RACE, and SQuAD benchmarks while having fewer parameters compared to BERT-large.''
 *
 * '''Tips:'''
 * ALBERT uses repeating layers which results in a small memory footprint,
 * however the computational cost remains similar to a BERT-like architecture with
 * the same number of hidden layers as it has to iterate through the same number of (repeating) layers.
 *
 * ==Example==
 * {{{
 * import spark.implicits._
 * import com.johnsnowlabs.nlp.base.DocumentAssembler
 * import com.johnsnowlabs.nlp.annotators.Tokenizer
 * import com.johnsnowlabs.nlp.embeddings.AlbertEmbeddings
 * import com.johnsnowlabs.nlp.EmbeddingsFinisher
 * import org.apache.spark.ml.Pipeline
 *
 * val documentAssembler = new DocumentAssembler()
 *   .setInputCol("text")
 *   .setOutputCol("document")
 *
 * val tokenizer = new Tokenizer()
 *   .setInputCols("document")
 *   .setOutputCol("token")
 *
 * val embeddings = AlbertEmbeddings.pretrained()
 *   .setInputCols("token", "document")
 *   .setOutputCol("embeddings")
 *
 * val embeddingsFinisher = new EmbeddingsFinisher()
 *   .setInputCols("embeddings")
 *   .setOutputCols("finished_embeddings")
 *   .setOutputAsVector(true)
 *   .setCleanAnnotations(false)
 *
 * val pipeline = new Pipeline().setStages(Array(
 *   documentAssembler,
 *   tokenizer,
 *   embeddings,
 *   embeddingsFinisher
 * ))
 *
 * val data = Seq("This is a sentence.").toDF("text")
 * val result = pipeline.fit(data).transform(data)
 *
 * result.selectExpr("explode(finished_embeddings) as result").show(5, 80)
 * +--------------------------------------------------------------------------------+
 * |                                                                          result|
 * +--------------------------------------------------------------------------------+
 * |[1.1342473030090332,-1.3855540752410889,0.9818322062492371,-0.784737348556518...|
 * |[0.847029983997345,-1.047153353691101,-0.1520637571811676,-0.6245765686035156...|
 * |[-0.009860038757324219,-0.13450059294700623,2.707749128341675,1.2916892766952...|
 * |[-0.04192575812339783,-0.5764210224151611,-0.3196685314178467,-0.527840495109...|
 * |[0.15583214163780212,-0.1614152491092682,-0.28423872590065,-0.135491415858268...|
 * +--------------------------------------------------------------------------------+
 * }}}
 *
 * @see [[com.johnsnowlabs.nlp.annotators.classifier.dl.AlbertForTokenClassification AlbertForTokenClassification]] for
 *      AlbertEmbeddings with a token classification layer on top
 * @see [[https://nlp.johnsnowlabs.com/docs/en/annotators Annotators Main Page]] for a list of transformer based embeddings
 * @param uid required uid for storing annotator to disk
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
 * @groupdesc param A list of (hyper-)parameter keys this annotator can take. Users can set and get the parameter values through setters and getters, respectively.
 */
class AlbertEmbeddings(override val uid: String) extends AnnotatorModel[AlbertEmbeddings]
    with TensorflowParams[AlbertEmbeddings]
    with WriteTensorflowModel
    with WritePytorchModel
    with WriteSentencePieceModel
    with HasBatchedAnnotate[AlbertEmbeddings]
    with HasEmbeddingsProperties
    with HasCaseSensitiveProperties
    with HasStorageRef {

  /** Annotator reference id. Used to identify elements in metadata or to refer to this annotator type */
  def this() = this(Identifiable.randomUID("ALBERT_EMBEDDINGS"))

  /**
   * Input Annotator Types: DOCUMENT, TOKEN
   *
   * @group anno
   */
  override val inputAnnotatorTypes: Array[String] = Array(AnnotatorType.DOCUMENT, AnnotatorType.TOKEN)
  /**
   * Output Annotator Types: WORD_EMBEDDINGS
   *
   * @group anno
   */
  override val outputAnnotatorType: AnnotatorType = AnnotatorType.WORD_EMBEDDINGS

  /**
   * Max sentence length to process (Default: `128`)
   *
   * @group param
   */
  val maxSentenceLength = new IntParam(this, "maxSentenceLength", "Max sentence length to process")

  val deepLearningEngine = new Param[String](this, "deepLearningEngine",
    "Deep Learning engine for creating embeddings [tensorflow|pytorch]")

  private var tfModel: Option[Broadcast[TensorflowAlbert]] = None
  private var pytorchModel: Option[Broadcast[PytorchAlbert]] = None

  /** @group setParam */
  def setMaxSentenceLength(value: Int): this.type = {
    require(value <= 512, "ALBERT models do not support sequences longer than 512 because of trainable positional embeddings")
    require(value >= 1, "The maxSentenceLength must be at least 1")
    set(maxSentenceLength, value)
    this
  }

  def setDeepLearningEngine(value: String): this.type = {
    set(deepLearningEngine, value)
  }

  /** @group getParam */
  def getMaxSentenceLength: Int = $(maxSentenceLength)

  /** @group setParam */
  override def setDimension(value: Int): this.type = {
    if (get(dimension).isEmpty)
      set(this.dimension, value)
    this
  }
  /** @group setParam */
  def setModelIfNotSet(spark: SparkSession, tensorflowWrapper: TensorflowWrapper, spp: SentencePieceWrapper): AlbertEmbeddings = {
    if (tfModel.isEmpty) {

      tfModel = Some(
        spark.sparkContext.broadcast(
          new TensorflowAlbert(
            tensorflowWrapper,
            spp,
            batchSize = $(batchSize),
            configProtoBytes = getConfigProtoBytes,
            signatures = getSignatures
          )
        )
      )
    }

    this
  }

  def setPytorchModelIfNotSet(spark: SparkSession, pytorchWrapper: PytorchWrapper,
                              sentencePieceWrapper: SentencePieceWrapper): AlbertEmbeddings = {
    if (pytorchModel.isEmpty) {
      pytorchModel = Some(spark.sparkContext.broadcast(
        new PytorchAlbert(pytorchWrapper, sentencePieceWrapper)
      ))
    }
    this
  }

  setDefault(
    batchSize -> 32,
    dimension -> 768,
    maxSentenceLength -> 128,
    caseSensitive -> false,
    deepLearningEngine -> "tensorflow"
  )

  def getModelIfNotSet: TensorflowAlbert = tfModel.get.value

  def getPytorchModelIfNotSet: PytorchAlbert = pytorchModel.get.value

  def getDeepLearningEngine: String = $(deepLearningEngine).toLowerCase

  /**
   * takes a document and annotations and produces new annotations of this annotator's annotation type
   *
   * @param batchedAnnotations Annotations that correspond to inputAnnotationCols generated by previous annotators if any
   * @return any number of annotations processed for every input annotation. Not necessary one to one relationship
   */
  override def batchAnnotate(batchedAnnotations: Seq[Array[Annotation]]): Seq[Seq[Annotation]] = {

    val batchedTokenizedSentences: Array[Array[TokenizedSentence]] = batchedAnnotations.map(annotations =>
      TokenizedWithSentence.unpack(annotations).toArray
    ).toArray

    /*Return empty if the real tokens are empty*/
    if (batchedTokenizedSentences.nonEmpty) {
      batchedTokenizedSentences.map(tokenizedSentences => {

        val embeddings = getDeepLearningEngine match {
          case "tensorflow" => {
            getModelIfNotSet.predict(tokenizedSentences, $(batchSize), $(maxSentenceLength), $(caseSensitive))
          }
          case "pytorch" => {
            getPytorchModelIfNotSet.predict(tokenizedSentences, $(batchSize), $(maxSentenceLength), $(caseSensitive))
          }
          case _ => throw new IllegalArgumentException(s"Deep learning engine $getDeepLearningEngine not supported")
        }

        WordpieceEmbeddingsSentence.pack(embeddings)
      })

    } else {
      Seq(Seq.empty[Annotation])
    }

  }

  override def onWrite(path: String, spark: SparkSession): Unit = {
    super.onWrite(path, spark)
    getDeepLearningEngine match {
      case "tensorflow" => {
        writeTensorflowModelV2(path, spark, getModelIfNotSet.tensorflow, "_albert",
          AlbertEmbeddings.tfFile, configProtoBytes = getConfigProtoBytes)
        writeSentencePieceModel(path, spark, getModelIfNotSet.spp, "_albert", AlbertEmbeddings.sppFile)
      }
      case "pytorch" => {
        writePytorchModel(path, spark, getPytorchModelIfNotSet.pytorchWrapper, AlbertEmbeddings.torchscriptFile)
        writeSentencePieceModel(path, spark, getPytorchModelIfNotSet.sentencePieceWrapper,
                         "_albert", AlbertEmbeddings.sppFile)
      }
    }

  }

  override protected def afterAnnotate(dataset: DataFrame): DataFrame = {
    dataset.withColumn(getOutputCol, wrapEmbeddingsMetadata(dataset.col(getOutputCol), $(dimension), Some($(storageRef))))
  }

}

trait ReadablePretrainedAlbertModel extends ParamsAndFeaturesReadable[AlbertEmbeddings] with HasPretrained[AlbertEmbeddings] {
  override val defaultModelName: Some[String] = Some("albert_base_uncased")

  /** Java compliant-overrides */
  override def pretrained(): AlbertEmbeddings = super.pretrained()

  override def pretrained(name: String): AlbertEmbeddings = super.pretrained(name)

  override def pretrained(name: String, lang: String): AlbertEmbeddings = super.pretrained(name, lang)

  override def pretrained(name: String, lang: String, remoteLoc: String): AlbertEmbeddings = super.pretrained(name, lang, remoteLoc)
}

trait ReadAlbertModel extends LoadModel[AlbertEmbeddings]
  with ParamsAndFeaturesReadable[AlbertEmbeddings]
  with ReadTensorflowModel
  with ReadSentencePieceModel
  with ReadPytorchModel {

  override val tfFile: String = "albert_tensorflow"
  override val sppFile: String = "albert_spp"
  override val torchscriptFile: String = "albert_pytorch"

  addReader(readModel)

  def readModel(instance: AlbertEmbeddings, path: String, spark: SparkSession): Unit = {
    val sentencePieceWrapper = readSentencePieceModel(path, spark, "_albert_spp", sppFile)
    instance.getDeepLearningEngine match {
      case "tensorflow" => {
        val tf = readTensorflowModel(path, spark, "_albert_tf")
        instance.setModelIfNotSet(spark, tf, sentencePieceWrapper)
      }
      case "pytorch" => {
        val pytorchWrapper = readPytorchModel(s"$path/$torchscriptFile", spark, "_albert")
        instance.setPytorchModelIfNotSet(spark, pytorchWrapper, sentencePieceWrapper)
      }
      case _ => throw new IllegalArgumentException(s"Deep learning engine ${instance.getDeepLearningEngine} not supported")
    }
  }

  override def createEmbeddingsFromTensorflow(tfWrapper: TensorflowWrapper, signatures: Map[String, String],
                                              tfModelPath: String, spark: SparkSession): AlbertEmbeddings =  {

    val sentencePieceWrapper = loadSentencepiece(tfModelPath, "tensorflow")

    /** the order of setSignatures is important is we use getSignatures inside setModelIfNotSet */
    new AlbertEmbeddings()
      .setSignatures(signatures)
      .setModelIfNotSet(spark, tfWrapper, sentencePieceWrapper)
  }

  override def createEmbeddingsFromPytorch(pytorchWrapper: PytorchWrapper, torchModelPath: String,
                                           spark: SparkSession): AlbertEmbeddings = {

    val sentencePieceWrapper = loadSentencepiece(torchModelPath, "pytorch")

    new AlbertEmbeddings()
      .setPytorchModelIfNotSet(spark, pytorchWrapper, sentencePieceWrapper)
  }

  def loadSentencepiece(tfModelPath: String, engine: String): SentencePieceWrapper = {
    val sppModelPath = if(engine == "pytorch") tfModelPath else tfModelPath + "/assets"
    val sppModel = new File(sppModelPath, "spiece.model")
    require(sppModel.exists(), s"SentencePiece model spiece.model not found in folder $sppModelPath")

    SentencePieceWrapper.read(sppModel.toString)
  }

}


/**
 * This is the companion object of [[AlbertEmbeddings]]. Please refer to that class for the documentation.
 */
object AlbertEmbeddings extends ReadablePretrainedAlbertModel with ReadAlbertModel
