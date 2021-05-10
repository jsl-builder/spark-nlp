package com.johnsnowlabs.nlp.annotators.parser.dl

import com.johnsnowlabs.nlp.AnnotatorApproach
import com.johnsnowlabs.nlp.AnnotatorType.{DEPENDENCY, DOCUMENT, TOKEN}
import com.johnsnowlabs.nlp.util.io.SparkSqlHelper
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions.col

class DependencyParserDLApproach(override val uid: String) extends AnnotatorApproach[DependencyParserDLModel] {

  def this() = this(Identifiable.randomUID(DEPENDENCY))

  override val description: String = "Dependency Parser DL finds a grammatical relation between two words in a sentence"

  override def train(dataset: Dataset[_], recursivePipeline: Option[PipelineModel]): DependencyParserDLModel = {
    //TODO: Check if column embeddings are in the dataset to avoid computing vocabulary
    //TODO: Control that either dataset is sent with CoNLLU format or create a parameter for lemma.result
    //TODO: Add a parameter to take a sample from dataset to compute vocabulary
    val dataSetWithUniqueWords =
      SparkSqlHelper.uniqueArrayElements(dataset.withColumn("words", col("lemma.result")), "words")

    val uniqueWords = Seq("*INITIAL*") ++ dataSetWithUniqueWords.select("unique_words_elements").rdd.map(rows =>
      rows.getSeq(0).asInstanceOf[Seq[String]]).collect().flatten.distinct

    val vocabulary: Map[String, Int] = uniqueWords.zipWithIndex.map( word => (word._1, word._2)).toMap

    new DependencyParserDLModel()
      .setVocabulary(vocabulary)
  }

  /** Annotator reference id. Used to identify elements in metadata or to refer to this annotator type */
  override val inputAnnotatorTypes: Array[String] = Array(DOCUMENT, TOKEN)
  override val outputAnnotatorType: AnnotatorType = DEPENDENCY
}
