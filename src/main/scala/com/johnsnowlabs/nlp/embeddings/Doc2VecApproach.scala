/*
 * Copyright 2017-2021 John Snow Labs
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

import com.johnsnowlabs.nlp.AnnotatorType.{SENTENCE_EMBEDDINGS, TOKEN}
import com.johnsnowlabs.nlp.AnnotatorApproach
import com.johnsnowlabs.storage.HasStorageRef

import org.apache.spark.ml.PipelineModel
import org.apache.spark.mllib.feature.Word2Vec
import org.apache.spark.ml.param.{DoubleParam, IntParam, ParamValidators}
import org.apache.spark.ml.util.{DefaultParamsReadable, Identifiable}
import org.apache.spark.sql.{Dataset, SparkSession}

/**
 * Word2Vec creates vector representation of words in a text corpus.
 * The algorithm first constructs a vocabulary from the corpus
 * and then learns vector representation of words in the vocabulary.
 * The vector representation can be used as features in
 * natural language processing and machine learning algorithms.
 *
 * We use Word2Vec implemented in Spark ML. They used skip-gram model in our implementation and hierarchical softmax
 * method to train the model. The variable names in the implementation
 * matches the original C implementation.
 *
 * For original C implementation, see https://code.google.com/p/word2vec/
 * For research papers, see
 * Efficient Estimation of Word Representations in Vector Space
 * and Distributed Representations of Words and Phrases and their Compositionality.
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
 * @groupdesc param A list of (hyper-)parameter keys this annotator can take. Users can set and get the parameter values through setters and getters, respectively.
 */
class Doc2VecApproach(override val uid: String)
  extends AnnotatorApproach[Doc2VecModel]
    with HasStorageRef {

  def this() = this(Identifiable.randomUID("Doc2VecApproach"))

  override val description = "Distributed Representations of Words and Phrases and their Compositionality"

  /** Input Annotator Types: TOKEN
   *
   * @group anno
   */
  override val inputAnnotatorTypes: Array[AnnotatorType] = Array(TOKEN)

  /** Output Annotator Types: SENTENCE_EMBEDDINGS
   *
   * @group anno
   */
  override val outputAnnotatorType: String = SENTENCE_EMBEDDINGS

  /**
   * The dimension of the code that you want to transform from words.
   * Default: 100
   *
   * @group param
   */
  val vectorSize = new IntParam(
    this, "vectorSize", "the dimension of codes after transforming from words (> 0)",
    ParamValidators.gt(0))

  /** @group setParam */
  def setVectorSize(value: Int): this.type = set(vectorSize, value)

  /** @group getParam */
  def getVectorSize: Int = $(vectorSize)

  /**
   * The window size (context words from [-window, window]).
   * Default: 5
   *
   * @group expertParam
   */
  val windowSize = new IntParam(
    this, "windowSize", "the window size (context words from [-window, window]) (> 0)",
    ParamValidators.gt(0))

  /** @group expertSetParam */
  def setWindowSize(value: Int): this.type = set(windowSize, value)

  /** @group expertGetParam */
  def getWindowSize: Int = $(windowSize)

  /**
   * Number of partitions for sentences of words.
   * Default: 1
   *
   * @group param
   */
  val numPartitions = new IntParam(
    this, "numPartitions", "number of partitions for sentences of words (> 0)",
    ParamValidators.gt(0))

  /** @group setParam */
  def setNumPartitions(value: Int): this.type = set(numPartitions, value)

  /** @group getParam */
  def getNumPartitions: Int = $(numPartitions)

  /**
   * The minimum number of times a token must appear to be included in the word2vec model's
   * vocabulary.
   * Default: 5
   *
   * @group param
   */
  val minCount = new IntParam(this, "minCount", "the minimum number of times a token must " +
    "appear to be included in the word2vec model's vocabulary (>= 0)", ParamValidators.gtEq(0))

  /** @group setParam */
  def setMinCount(value: Int): this.type = set(minCount, value)

  /** @group getParam */
  def getMinCount: Int = $(minCount)

  /**
   * Sets the maximum length (in words) of each sentence in the input data.
   * Any sentence longer than this threshold will be divided into chunks of
   * up to `maxSentenceLength` size.
   * Default: 1000
   *
   * @group param
   */
  val maxSentenceLength = new IntParam(this, "maxSentenceLength", "Maximum length " +
    "(in words) of each sentence in the input data. Any sentence longer than this threshold will " +
    "be divided into chunks up to the size (> 0)", ParamValidators.gt(0))

  /** @group setParam */
  def setMaxSentenceLength(value: Int): this.type = set(maxSentenceLength, value)

  /** @group getParam */
  def getMaxSentenceLength: Int = $(maxSentenceLength)

  /**
   * Param for Step size to be used for each iteration of optimization (&gt; 0).
   *
   * @group param
   */
  val stepSize: DoubleParam = new DoubleParam(this, "stepSize", "Step size (learning rate) to be used for each iteration of optimization (> 0)", ParamValidators.gt(0))

  /** @group setParam */
  def setStepSize(value: Double): this.type = set(stepSize, value)

  /** @group getParam */
  def getStepSize: Double = $(stepSize)

  /**
   * Param for maximum number of iterations (&gt;= 0).
   *
   * @group param
   */
  val maxIter: IntParam = new IntParam(this, "maxIter", "maximum number of iterations (>= 0)", ParamValidators.gtEq(0))

  /** @group setParam */
  def setMaxIter(value: Int): this.type = set(maxIter, value)

  /** @group getParam */
  def getMaxIter: Int = $(maxIter)

  /** Random seed for shuffling the dataset
   *
   * @group param
   * */
  val seed = new IntParam(this, "seed", "Random seed")

  /** @group setParam */
  def setSeed(value: Int): Doc2VecApproach.this.type = set(seed, value)

  setDefault(
    vectorSize -> 100,
    windowSize -> 5,
    numPartitions -> 1,
    minCount -> 1,
    maxSentenceLength -> 1000,
    stepSize -> 0.025,
    maxIter -> 1,
    seed -> 44
  )

  override def beforeTraining(spark: SparkSession): Unit = {}

  override def train(dataset: Dataset[_], recursivePipeline: Option[PipelineModel]): Doc2VecModel = {

    val tokenResult: String = ".result"
    val inputColumns = getInputCols(0) + tokenResult

    val word2Vec = new Word2Vec()
      .setLearningRate($(stepSize))
      .setMinCount($(minCount))
      .setNumIterations($(maxIter))
      .setNumPartitions($(numPartitions))
      .setVectorSize($(vectorSize))
      .setWindowSize($(windowSize))
      .setMaxSentenceLength($(maxSentenceLength))
      .setSeed($(seed))

    val input = dataset.select(dataset.col(inputColumns)).rdd.map(r => r.getSeq[String](0))

    val model = word2Vec.fit(input)

    new Doc2VecModel()
      .setWordVectors(model.getVectors)
      .setVectorSize($(vectorSize))
      .setStorageRef($(storageRef))
      .setDimension($(vectorSize))

  }

}

/**
 * This is the companion object of [[Doc2VecApproach]]. Please refer to that class for the documentation.
 */
object Doc2VecApproach extends DefaultParamsReadable[Doc2VecApproach]
