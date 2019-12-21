package com.johnsnowlabs.nlp.embeddings

import com.johnsnowlabs.nlp.EmbeddingsFinisher
import com.johnsnowlabs.nlp.annotators.sbd.pragmatic.SentenceDetector
import com.johnsnowlabs.nlp.base.{DocumentAssembler, RecursivePipeline}
import com.johnsnowlabs.nlp.util.io.ResourceHelper
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{BucketedRandomProjectionLSH, BucketedRandomProjectionLSHModel, Normalizer, SQLTransformer}
import org.apache.spark.sql.expressions.Window
import org.scalatest._
import org.apache.spark.sql.functions._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}

class UniversalSentenceEncoderTestSpec extends FlatSpec {

  "UniversalSentenceEncoder" should "correctly calculate sentence embeddings for a sentence" ignore {

    val smallCorpus = ResourceHelper.spark.read.option("header","true")
      .csv("src/test/resources/embeddings/sentence_embeddings.csv")

    val documentAssembler = new DocumentAssembler()
      .setInputCol("text")
      .setOutputCol("document")

    val sentence = new SentenceDetector()
      .setInputCols("document")
      .setOutputCol("sentence")

    val useEmbeddings = UniversalSentenceEncoder.pretrained("tfhub_use_lg", "en")
      .setInputCols("document")
      .setOutputCol("sentence_embeddings")

    val pipeline = new RecursivePipeline()
      .setStages(Array(
        documentAssembler,
        sentence,
        useEmbeddings
      ))

    val pipelineDF = pipeline.fit(smallCorpus).transform(smallCorpus)
    pipelineDF.show()
    pipelineDF.select("sentence_embeddings.embeddings").show()
    pipelineDF.printSchema()

  }

  "UniversalSentenceEncoder" should "integrate into Spark ML" ignore {

    import ResourceHelper.spark.implicits._

    val smallCorpus = ResourceHelper.spark.read.option("header","true")
      .csv("src/test/resources/embeddings/sentence_embeddings_use.csv")

    val documentAssembler = new DocumentAssembler()
      .setInputCol("text")
      .setOutputCol("document")

    val useEmbeddings = UniversalSentenceEncoder.pretrained("tfhub_use_lg", "en")
      .setInputCols("document")
      .setOutputCol("sentence_embeddings")

    val sentenceFinisher = new EmbeddingsFinisher()
      .setInputCols("sentence_embeddings")
      .setOutputCols("sentence_embeddings_vectors")
      .setCleanAnnotations(false)
      .setOutputAsVector(true)

    val explodeVectors = new SQLTransformer().setStatement(
      "SELECT EXPLODE(sentence_embeddings_vectors) AS features, * FROM __THIS__")

    val vectorNormalizer = new Normalizer()
      .setInputCol("features")
      .setOutputCol("normFeatures")
      .setP(2L)

    val brp = new BucketedRandomProjectionLSH()
      .setBucketLength(100)
      .setNumHashTables(50)
      .setInputCol("normFeatures")
      .setOutputCol("hashes")

    val pipeline = new Pipeline()
      .setStages(Array(
        documentAssembler,
        useEmbeddings,
        sentenceFinisher,
        explodeVectors,
        vectorNormalizer,
        brp
      ))

    val pipelineModel = pipeline.fit(smallCorpus)
    val pipelineDF = pipelineModel.transform(smallCorpus)
      .withColumn("id", monotonically_increasing_id)

    pipelineDF.show()
    pipelineDF.select("features").show()

    pipelineDF.select("id", "text").show(false)

    val brpModel = pipelineModel.stages.last.asInstanceOf[BucketedRandomProjectionLSHModel]
    brpModel.approxSimilarityJoin(
      pipelineDF.select("normFeatures", "hashes", "id"),
      pipelineDF.select("normFeatures", "hashes", "id"),
      1.0,
      "EuclideanDistance")
      .select(
        $"datasetA.id".alias("idA"),
        $"datasetB.id".alias("idB"),
        $"EuclideanDistance")
      .filter("idA != idB") // not interested in self evaluation!
      .orderBy($"EuclideanDistance".asc)
      .show()
  }

}
