package com.johnsnowlabs.nlp.annotators.seq2seq

import com.johnsnowlabs.ml.tensorflow.sentencepiece.ReadSentencePieceModel
import com.johnsnowlabs.nlp.{Annotation, AnnotatorModel, HasBatchedAnnotate, HasPretrained, HasSimpleAnnotate, ParamsAndFeaturesReadable, ParamsAndFeaturesWritable}
import com.johnsnowlabs.ml.tensorflow.{ReadTensorflowModel, TensorflowGPT2, TensorflowWrapper, WriteTensorflowModel}
import com.johnsnowlabs.nlp.AnnotatorType.DOCUMENT
import com.johnsnowlabs.nlp.annotators.tokenizer.bpe.{BpeTokenizer, Gpt2Tokenizer}
import com.johnsnowlabs.nlp.serialization.MapFeature
import com.johnsnowlabs.nlp.util.io.{ExternalResource, ReadAs, ResourceHelper}

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.param.{BooleanParam, DoubleParam, IntArrayParam, IntParam, Param}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.SparkSession

import java.io.File


/**
 * GPT-2: the OpenAI Text-To-Text Transformer
 *
 * GPT-2 is a large transformer-based language model with 1.5 billion parameters, trained on a dataset of 8 million
 * web pages. GPT-2 is trained with a simple objective: predict the next word, given all of the previous words within
 * some text. The diversity of the dataset causes this simple goal to contain naturally occurring demonstrations of
 * many tasks across diverse domains. GPT-2 is a direct scale-up of GPT, with more than 10X the parameters and trained
 * on more than 10X the amount of data.
 *
 * GPT-2 displays a broad set of capabilities, including the ability to generate conditional synthetic text samples of
 * unprecedented quality, where we prime the model with an input and have it generate a lengthy continuation. In
 * addition, GPT-2 outperforms other language models trained on specific domains (like Wikipedia, news, or books)
 * without needing to use these domain-specific training datasets. On language tasks like question answering, reading
 * comprehension, summarization, and translation, GPT-2 begins to learn these tasks from the raw text, using no
 * task-specific training data. While scores on these downstream tasks are far from state-of-the-art, they suggest
 * that the tasks can benefit from unsupervised techniques, given sufficient (unlabeled) data and compute.
 *
 * Pretrained models can be loaded with `pretrained` of the companion object:
 * {{{
 * val gpt2 = GPT2Transformer.pretrained()
 *   .setInputCols("document")
 *   .setOutputCol("generation")
 * }}}
 * The default model is `"gpt2"`, if no name is provided.
 * For available pretrained models please see the [[https://nlp.johnsnowlabs.com/models?q=gpt2 Models Hub]].
 *
 * For extended examples of usage, see [[https://github.com/JohnSnowLabs/spark-nlp/blob/master/src/test/scala/com/johnsnowlabs/nlp/annotators/seq2seq/GPT2TestSpec.scala GPT2TestSpec]].
 *
 * '''Sources:'''
 *  - [[https://d4mucfpksywv.cloudfront.net/better-language-models/language_models_are_unsupervised_multitask_learners.pdf Language Models are Unsupervised Multitask Learners]]
 *  - [[https://github.com/openai/gpt-2]]
 *
 * '''Paper Abstract:'''
 *
 * ''Natural language processing tasks, such as question answering, machine translation, reading comprehension, and
 * summarization, are typically approached with supervised learning on taskspecific datasets. We demonstrate that
 * language models begin to learn these tasks without any explicit supervision when trained on a new dataset
 * of millions of webpages called WebText. When conditioned on a document plus questions, the answers generated by
 * the language model reach F1 on the CoQA dataset - matching or exceeding the performance of 3 out of 4 baseline
 * systems without using the 127,000+ training examples. The capacity of the language model is essential to the
 * success of zero-shot task transfer and increasing it improves performance in a log-linear fashion across tasks.
 * Our largest model, GPT-2, is a 1.5B parameter Transformer that achieves state of the art results on 7 out of 8
 * tested language modeling datasets in a zero-shot setting but still underfits WebText. Samples from the model
 * reflect these improvements and contain coherent paragraphs of text. These findings suggest a promising path
 * towards building language processing systems which learn to perform tasks from their naturally occurring
 * demonstrations.''
 *
 * '''Note:'''
 *
 * This is a very computationally expensive module especially on larger sequence.
 * The use of an accelerator such as GPU is recommended.
 *
 * ==Example==
 * {{{
 * import spark.implicits._
 * import com.johnsnowlabs.nlp.base.DocumentAssembler
 * import com.johnsnowlabs.nlp.annotators.seq2seq.GPT2Transformer
 * import org.apache.spark.ml.Pipeline
 *
 * val documentAssembler = new DocumentAssembler()
 *   .setInputCol("text")
 *   .setOutputCol("documents")
 *
 * val gpt2 = GPT2Transformer.pretrained("gpt2")
 *   .setInputCols(Array("documents"))
 *   .setMinOutputLength(10)
 *   .setMaxOutputLength(50)
 *   .setDoSample(false)
 *   .setTopK(50)
 *   .setNoRepeatNgramSize(3)
 *   .setOutputCol("generation")
 *
 * val pipeline = new Pipeline().setStages(Array(documentAssembler, gpt2))
 *
 * val data = Seq(
 *   "My name is Leonardo."
 * ).toDF("text")
 * val result = pipeline.fit(data).transform(data)
 *
 * results.select("generation.result").show(truncate = false)
 * +----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
 * |result                                                                                                                                                                                              |
 * +----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
 * |[ My name is Leonardo. I am a man of letters. I have been a man for many years. I was born in the year 1776. I came to the United States in 1776, and I have lived in the United Kingdom since 1776]|
 * +----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
 * }}}
 *
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

class GPT2Transformer(override val uid: String)
  extends AnnotatorModel[GPT2Transformer]
    with HasSimpleAnnotate[GPT2Transformer]
    with HasBatchedAnnotate[GPT2Transformer]
    with ParamsAndFeaturesWritable
    with WriteTensorflowModel {

  def this() = this(Identifiable.randomUID("GPT2TRANSFORMER"))

  /** Input annotator type : DOCUMENT
   *
   * @group param
   * */
  override val inputAnnotatorTypes: Array[AnnotatorType] = Array(DOCUMENT)

  /** Output annotator type : DOCUMENT
   *
   * @group param
   * */
  override val outputAnnotatorType: String = DOCUMENT

  /**
   * Set transformer task, e.g. `"summarize:"` (Default: `""`).
   *
   * @group param
   */
  val task = new Param[String](this, "task", "Set transformer task, e.g. 'summarize'")

  /** @group setParam */
  def setTask(value: String): GPT2Transformer.this.type = {
    if (get(task).isEmpty)
      set(task, value)
    this
  }

  /**
   * Minimum length of the sequence to be generated (Default: `0`)
   *
   * @group param
   */
  val minOutputLength = new IntParam(this, "minOutputLength", "Minimum length of the sequence to be generated")

  /** @group setParam */
  def setMinOutputLength(value: Int): GPT2Transformer.this.type = {
    set(minOutputLength, value)
    this
  }

  /** @group getParam */
  def getMinOutputLength: Int = $(this.minOutputLength)

  /**
   * Maximum length of the sequence to be generated (Default: `20`)
   *
   * @group param
   */
  val maxOutputLength = new IntParam(this, "maxOutputLength", "Maximum length of the sequence to be generated")

  /** @group setParam */
  def setMaxOutputLength(value: Int): GPT2Transformer.this.type = {
    set(maxOutputLength, value)
    this
  }

  /** @group getParam */
  def getMaxOutputLength: Int = $(this.maxOutputLength)

  /**
   * Whether or not to use sampling, use greedy decoding otherwise (Default: `false`)
   *
   * @group param
   */
  val doSample = new BooleanParam(this, "doSample", "Whether or not to use sampling; use greedy decoding otherwise")

  /** @group setParam */
  def setDoSample(value: Boolean): GPT2Transformer.this.type = {
    set(doSample, value)
    this
  }

  /** @group getParam */
  def getDoSample: Boolean = $(this.doSample)

  /**
   * The value used to module the next token probabilities (Default: `1.0`)
   *
   * @group param
   */
  val temperature = new DoubleParam(this, "temperature", "The value used to module the next token probabilities")

  /** @group setParam */
  def setTemperature(value: Double): GPT2Transformer.this.type = {
    set(temperature, value)
    this
  }

  /** @group getParam */
  def getTemperature: Double = $(this.temperature)

  /**
   * The number of highest probability vocabulary tokens to keep for top-k-filtering (Default: `50`)
   *
   * @group param
   */
  val topK = new IntParam(this, "topK", "The number of highest probability vocabulary tokens to keep for top-k-filtering")

  /** @group setParam */
  def setTopK(value: Int): GPT2Transformer.this.type = {
    set(topK, value)
    this
  }

  /** @group getParam */
  def getTopK: Int = $(this.topK)

  /**
   * If set to float < `1.0`, only the most probable tokens with probabilities that add up to `topP` or higher are kept
   * for generation (Default: `1.0`)
   *
   * @group param
   */
  val topP = new DoubleParam(this, "topP", "If set to float < 1, only the most probable tokens with probabilities that add up to ``top_p`` or higher are kept for generation")

  /** @group setParam */
  def setTopP(value: Double): GPT2Transformer.this.type = {
    set(topP, value)
    this
  }

  /** @group getParam */
  def getTopP: Double = $(this.topP)

  /**
   * The parameter for repetition penalty (Default: `1.0`).
   * `1.0` means no penalty. See [[https://arxiv.org/pdf/1909.05858.pdf this paper]] for more details.
   *
   * @group param
   */
  val repetitionPenalty = new DoubleParam(this, "repetitionPenalty", "The parameter for repetition penalty. 1.0 means no penalty.")

  /** @group setParam */
  def setRepetitionPenalty(value: Double): GPT2Transformer.this.type = {
    set(repetitionPenalty, value)
    this
  }

  /** @group getParam */
  def getRepetitionPenalty: Double = $(this.repetitionPenalty)

  /**
   * If set to int > `0`, all ngrams of that size can only occur once (Default: `0`)
   *
   * @group param
   */
  val noRepeatNgramSize = new IntParam(this, "noRepeatNgramSize", "If set to int > 0, all ngrams of that size can only occur once")

  /** @group setParam */
  def setNoRepeatNgramSize(value: Int): GPT2Transformer.this.type = {
    set(noRepeatNgramSize, value)
    this
  }

  /** @group getParam */
  def getNoRepeatNgramSize: Int = $(this.noRepeatNgramSize)

  /**
   * Optional Random seed for the model. Needs to be of type `Long`.
   *
   * @group param
   */
  var randomSeed: Option[Int] = None

  /** @group setParam */
  def setRandomSeed(value: Int): GPT2Transformer.this.type = {
    if (randomSeed.isEmpty) {
      this.randomSeed = Some(value)
    }
    this
  }

  /** @group getParam */
  def getRandomSeed: Option[Int] = this.randomSeed

  /**
   * A list of token ids which are ignored in the decoder's output
   *
   * @group param
   * */
  var ignoreTokenIds = new IntArrayParam(this, "ignoreTokenIds", "A list of token ids which are ignored in the decoder's output")

  /** @group setParam */
  def setIgnoreTokenIds(tokenIds: Array[Int]): GPT2Transformer.this.type = {
    set(ignoreTokenIds, tokenIds)
  }

  /** @group getParam */
  def getIgnoreTokenIds: Array[Int] = $(ignoreTokenIds)

  /**
   * ConfigProto from tensorflow, serialized into byte array. Get with config_proto.SerializeToString()
   *
   * @group param
   */
  val configProtoBytes = new IntArrayParam(this, "configProtoBytes", "ConfigProto from tensorflow, serialized into byte array. Get with config_proto.SerializeToString()")

  /** @group setParam */
  def setConfigProtoBytes(bytes: Array[Int]): GPT2Transformer.this.type = set(this.configProtoBytes, bytes)

  /** @group getParam */
  def getConfigProtoBytes: Option[Array[Byte]] = get(this.configProtoBytes).map(_.map(_.toByte))

  private var _tfModel: Option[Broadcast[TensorflowGPT2]] = None


  /**
   * Vocabulary used to encode the words to ids with bpeTokenizer.encode
   *
   * @group param
   * */
  val vocabulary: MapFeature[String, Int] = new MapFeature(this, "vocabulary")


  /** @group setParam */
  def setVocabulary(value: Map[String, Int]): this.type = set(vocabulary, value)

  /**
   * Holding merges.txt coming from RoBERTa model
   *
   * @group param
   */
  val merges: MapFeature[(String, String), Int] = new MapFeature(this, "merges")

  /** @group setParam */
  def setMerges(value: Map[(String, String), Int]): this.type = set(merges, value)

  /** @group setParam */
  def setModelIfNotSet(spark: SparkSession, tfWrapper: TensorflowWrapper): this.type = {
    if (_tfModel.isEmpty) {

      val bpeTokenizer = BpeTokenizer.forModel(
        "gpt2",
        merges = $$(merges),
        vocab = $$(vocabulary),
        padWithSentenceTokens = false
      ).asInstanceOf[Gpt2Tokenizer]

      _tfModel = Some(
        spark.sparkContext.broadcast(
          new TensorflowGPT2(tfWrapper, bpeTokenizer, configProtoBytes = getConfigProtoBytes)
        )
      )
    }
    this
  }

  /** @group getParam */
  def getModelIfNotSet: TensorflowGPT2 = _tfModel.get.value

  setDefault(
    task -> "",
    minOutputLength -> 0,
    maxOutputLength -> 20,
    doSample -> false,
    temperature -> 1.0,
    topK -> 50,
    topP -> 1.0,
    repetitionPenalty -> 1.0,
    noRepeatNgramSize -> 0,
    ignoreTokenIds -> Array()
  )

  def annotate(annotations: Seq[Annotation]): Seq[Annotation] = {
    val nonEmptySentences = annotations.filter(_.result.nonEmpty)

    if (nonEmptySentences.nonEmpty) {
      this.getModelIfNotSet.generateSeq2Seq(
        sentences = nonEmptySentences,
        batchSize = 1,
        minOutputLength = $(minOutputLength),
        maxOutputLength = $(maxOutputLength),
        doSample = $(doSample),
        temperature = $(temperature),
        topK = $(topK),
        topP = $(topP),
        repetitionPenalty = $(repetitionPenalty),
        noRepeatNgramSize = $(noRepeatNgramSize),
        task = $(task),
        randomSeed = this.randomSeed,
        ignoreTokenIds = $(ignoreTokenIds)
      )
    } else {
      Seq.empty[Annotation]
    }
  }


  /**
   * takes a document and annotations and produces new annotations of this annotator's annotation type
   *
   * @param batchedAnnotations Annotations that correspond to inputAnnotationCols generated by previous annotators if any
   * @return any number of annotations processed for every input annotation. Not necessary one to one relationship
   */
  override def batchAnnotate(batchedAnnotations: Seq[Array[Annotation]]): Seq[Seq[Annotation]] = {
    val nonEmptyBatch = batchedAnnotations.filter(_.nonEmpty)

    if (nonEmptyBatch.nonEmpty) {
      nonEmptyBatch.map(batch => {
        val nonEmptyAnnotations = batch.filter(_.result.nonEmpty)
        if (nonEmptyAnnotations.nonEmpty) {
          this.getModelIfNotSet.generateSeq2Seq(
            sentences = nonEmptyAnnotations,
            batchSize = 1,
            minOutputLength = $(minOutputLength),
            maxOutputLength = $(maxOutputLength),
            doSample = $(doSample),
            temperature = $(temperature),
            topK = $(topK),
            topP = $(topP),
            repetitionPenalty = $(repetitionPenalty),
            noRepeatNgramSize = $(noRepeatNgramSize),
            task = $(task),
            randomSeed = this.randomSeed,
            ignoreTokenIds = $(ignoreTokenIds)
          )
        } else {
          Seq()
        }
      })
    } else {
      Seq()
    }
  }

  override def onWrite(path: String, spark: SparkSession): Unit = {
    super.onWrite(path, spark)
    writeTensorflowModelV2(path, spark, getModelIfNotSet.tensorflow, "_gpt2", GPT2Transformer.tfFile, configProtoBytes = getConfigProtoBytes)
  }
}

trait ReadablePretrainedGPT2TransformerModel extends ParamsAndFeaturesReadable[GPT2Transformer] with HasPretrained[GPT2Transformer] {
  override val defaultModelName: Some[String] = Some("gpt2")

  /** Java compliant-overrides */
  override def pretrained(): GPT2Transformer = super.pretrained()

  override def pretrained(name: String): GPT2Transformer = super.pretrained(name)

  override def pretrained(name: String, lang: String): GPT2Transformer = super.pretrained(name, lang)

  override def pretrained(name: String, lang: String, remoteLoc: String): GPT2Transformer = super.pretrained(name, lang, remoteLoc)
}

trait ReadGPT2TransformerTensorflowModel extends ReadTensorflowModel with ReadSentencePieceModel {
  this: ParamsAndFeaturesReadable[GPT2Transformer] =>

  override val tfFile: String = "gpt2_tensorflow"
  override val sppFile: String = "gpt2_"

  def readTensorflow(instance: GPT2Transformer, path: String, spark: SparkSession): Unit = {
    val tf = readTensorflowModel(path, spark, "_gpt2_tf")
    instance.setModelIfNotSet(spark, tf)
  }

  addReader(readTensorflow)

  def loadSavedModel(folder: String, spark: SparkSession): GPT2Transformer = {

    val f = new File(folder)
    val bpeFolder = folder + "/assets"

    val vocabFile = new File(s"$bpeFolder/vocab.txt")
    val mergesFile = new File(s"$bpeFolder/merges.txt")

    val savedModel = new File(folder, "saved_model.pb")

    require(f.exists, s"Folder $folder not found")
    require(f.isDirectory, s"File $folder is not a folder")
    require(
      savedModel.exists(),
      s"savedModel file saved_model.pb not found in folder $folder"
    )
    require(vocabFile.exists(), s"vocab.json not found in $bpeFolder")
    require(vocabFile.exists(), s"merges.txt not found in $bpeFolder")

    val vocabResource = new ExternalResource(vocabFile.getAbsolutePath, ReadAs.TEXT, Map("format" -> "text"))
    val words = ResourceHelper.parseLines(vocabResource).zipWithIndex.toMap

    val mergesResource = new ExternalResource(mergesFile.getAbsolutePath, ReadAs.TEXT, Map("format" -> "text"))
    val merges = ResourceHelper.parseLines(mergesResource)

    val bytePairs: Map[(String, String), Int] = merges.map(_.split(" "))
      .filter(w => w.length == 2)
      .map { case Array(c1, c2) => (c1, c2) }
      .zipWithIndex.toMap

    val (wrapper, _) = TensorflowWrapper.read(folder, zipped = false, useBundle = true, tags = Array("serve"))

    val gpt2model = new GPT2Transformer()
      .setMerges(bytePairs)
      .setVocabulary(words)
      .setModelIfNotSet(spark, wrapper)

    gpt2model
  }

}

object GPT2Transformer extends ReadablePretrainedGPT2TransformerModel with ReadGPT2TransformerTensorflowModel