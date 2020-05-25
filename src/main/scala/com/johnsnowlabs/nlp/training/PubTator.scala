package com.johnsnowlabs.nlp.training

import com.johnsnowlabs.nlp.annotator.{PerceptronModel, SentenceDetector, Tokenizer}
import com.johnsnowlabs.nlp.{Annotation, AnnotatorType, DocumentAssembler, Finisher}
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}


object PubTator {

  def readDataset(spark: SparkSession, path: String): DataFrame = {
    val pubtator = spark.sparkContext.textFile(path)
    val titles = pubtator.filter(x => x.contains("|a|") | x.contains("|t|"))
    val titlesText = titles.map(x => x.split("\\|")).groupBy(_.head)
      .map(x => (x._1.toInt, x._2.foldLeft(Seq[String]())((a, b) => a ++ Seq(b.last)))).map(x => (x._1, x._2.mkString(" ")))
    val df = spark.createDataFrame(titlesText).toDF("doc_id", "text")
    val docAsm = new DocumentAssembler().setInputCol("text").setOutputCol("document")
    val setDet = new SentenceDetector().setInputCols("document").setOutputCol("sentence")
    val tknz = new Tokenizer().setInputCols("sentence").setOutputCol("token")
    val pl = new Pipeline().setStages(Array(docAsm, setDet, tknz))
    val nlpDf = pl.fit(df).transform(df)
    val annotations = pubtator.filter(x => !x.contains("|a|") & !x.contains("|t|") & x.nonEmpty)
    val splitAnnotations = annotations.map(_.split("\\t")).map(x => (x(0), x(1).toInt, x(2).toInt - 1, x(3), x(4), x(5)))
    val docAnnotations = splitAnnotations.groupBy(_._1).map(x => (x._1, x._2))
      .map(x =>
        (x._1.toInt,
          x._2.zipWithIndex.map(a => (new Annotation(AnnotatorType.CHUNK, a._1._2, a._1._3, a._1._4, Map("entity" -> a._1._5, "chunk" -> a._2.toString), Array[Float]()))).toList
        )
      )
    val chunkMeta = new MetadataBuilder().putString("annotatorType", AnnotatorType.CHUNK).build()
    val annDf = spark.createDataFrame(docAnnotations).toDF("doc_id", "chunk")
      .withColumn("chunk", col("chunk").as("chunk", chunkMeta))
    val alignedDf = nlpDf.join(annDf, Seq("doc_id")).selectExpr("doc_id", "sentence", "token", "chunk")
    val iobTagging = udf((tokens: Seq[Row], chunkLabels: Seq[Row]) => {
      val tokenAnnotations = tokens.map(Annotation(_))
      val labelAnnotations = chunkLabels.map(Annotation(_))
      tokenAnnotations.map(ta => {
        val tokenLabel = labelAnnotations.filter(la => la.begin <= ta.begin && la.end >= ta.end).headOption
        val tokenTag = {
          if (tokenLabel.isEmpty) "O"
          else {
            val tokenCSV = tokenLabel.get.metadata.get("entity").get
            if (tokenCSV == "UnknownType") "O"
            else {
              val tokenPrefix = if (ta.begin == tokenLabel.get.begin) "B-" else "I-"
              val paddedTokenTag = "T" + "%03d".format(tokenCSV.split(",")(0).slice(1, 4).toInt)
              tokenPrefix + paddedTokenTag
            }
          }
        }

        Annotation(AnnotatorType.NAMED_ENTITY,
          ta.begin, ta.end,
          tokenTag,
          Map("word" -> ta.result)
        )
      }
      )
    })
    val labelMeta = new MetadataBuilder().putString("annotatorType", AnnotatorType.NAMED_ENTITY).build()
    val taggedDf = alignedDf.withColumn("label", iobTagging(col("token"), col("chunk")).as("label", labelMeta))

    val pos = PerceptronModel.pretrained().setInputCols(Array("sentence", "token")).setOutputCol("pos")
    val finisher = new Finisher().setInputCols("token", "pos", "label").setIncludeMetadata(true)
    val finishingPipeline = new Pipeline().setStages(Array(pos, finisher))
    finishingPipeline.fit(taggedDf).transform(taggedDf)
      .withColumnRenamed("finished_label", "finished_ner") //CoNLL generator expects finished_ner
  }
}