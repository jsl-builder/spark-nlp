package com.johnsnowlabs.nlp.util

import java.io.File

import com.johnsnowlabs.nlp.Finisher
import com.johnsnowlabs.nlp.annotators.ner.NerConverter
import com.johnsnowlabs.nlp.annotators.ner.dl.NerDLModel
import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import com.johnsnowlabs.nlp.util.io.ResourceHelper
import com.johnsnowlabs.util._
import org.apache.spark.ml.Pipeline
import org.scalatest._

import scala.io.Source
import scala.reflect.io.Directory

class CoNLLGeneratorTestSpec extends FlatSpec{
  ResourceHelper.spark
  import ResourceHelper.spark.implicits._ //for toDS and toDF

  val preModel = PretrainedPipeline("explain_document_dl", lang="en").model

  val finisherNoNER = new Finisher()
    .setInputCols("token", "pos")
    .setIncludeMetadata(true)

  val ourPipelineModel = new Pipeline()
    .setStages(Array(preModel, finisherNoNER))
    .fit(Seq("").toDF("text"))

  val testing = Seq(
    (1, "Google is a famous company"),
    (2, "Peter Parker is a super heroe"))
    .toDS.toDF( "_id", "text")

  val result = ourPipelineModel.transform(testing)


  //TODO: read this from a file?
  //this is what the output should be
  val testText = """"" "" "" ""
      |-DOCSTART- -X- -X- O
      |"" "" "" ""
      |"" "" "" ""
      |Google NNP NNP O
      |is VBZ VBZ O
      |a DT DT O
      |famous JJ JJ O
      |company NN NN O
      |"" "" "" ""
      |-DOCSTART- -X- -X- O
      |"" "" "" ""
      |"" "" "" ""
      |Peter NNP NNP O
      |Parker NNP NNP O
      |is VBZ VBZ O
      |a DT DT O
      |super JJ JJ O
      |heroe NN NN O""".stripMargin.replace("\n", "" )

  "The (dataframe, pipelinemodel, outputpath) generator" should "make the right file" in {

    //remove file if it's already there
    val directory = new Directory(new File("./testcsv"))
    directory.deleteRecursively()
    CoNLLGenerator.exportConllFiles(testing, ourPipelineModel, "./testcsv")

    //read csv, check if it's equal
    def getPath(dir: String): String = {
      val file = new File(dir)
      file.listFiles.filter(_.isFile)
        .filter(_.getName.endsWith(".csv"))
        .map(_.getPath).head
    }

    val filePath = getPath("./testcsv/")

    val fileContents = Source.fromFile(filePath).getLines.mkString

    directory.deleteRecursively()

    assert(fileContents==testText)
  }

  "The (dataframe, outputpath) generator" should "make the right file" in {
    //remove file if it's already there
    val directory = new Directory(new File("./testcsv"))
    directory.deleteRecursively()
    CoNLLGenerator.exportConllFiles(result, "./testcsv")

    //read csv, check if it's equal
    def getPath(dir: String): String = {
      val file = new File(dir)
      file.listFiles.filter(_.isFile)
        .filter(_.getName.endsWith(".csv"))
        .map(_.getPath).head
    }

    val filePath = getPath("./testcsv/")

    val fileContents = Source.fromFile(filePath).getLines.mkString

    directory.deleteRecursively()

    assert(fileContents==testText)
  }

  "The generator" should "make the right file with ners when appropriate" in {

    val ner = NerDLModel.pretrained("ner_dl")
      .setInputCols(Array("document", "token", "embeddings"))
      .setOutputCol("ner")

    val finisherWithNER = new Finisher()
      .setInputCols("token", "pos", "ner")
      .setIncludeMetadata(true)

    val ourNERPipelineModel = new Pipeline()
      .setStages(Array(preModel, ner, finisherWithNER))
      .fit(Seq("").toDF("text"))

    val result = ourNERPipelineModel.transform(testing)

    print(result.show(10, false))

    //remove file if it's already there
    val directory = new Directory(new File("./testcsv"))
    directory.deleteRecursively()

    CoNLLGenerator.exportConllFiles(result, "./testcsv")

    //read csv, check if it's equal
    def getPath(dir: String): String = {
      val file = new File(dir)
      file.listFiles.filter(_.isFile)
        .filter(_.getName.endsWith(".csv"))
        .map(_.getPath).head
    }

    val filePath = getPath("./testcsv/")

    val fileContents = Source.fromFile(filePath).getLines.mkString

   directory.deleteRecursively()

    val testNERText = """"" "" "" ""
                        |-DOCSTART- -X- -X- O
                        |"" "" "" ""
                        |"" "" "" ""
                        |Google NNP NNP B-ORG
                        |is VBZ VBZ O
                        |a DT DT O
                        |famous JJ JJ O
                        |company NN NN O
                        |"" "" "" ""
                        |-DOCSTART- -X- -X- O
                        |"" "" "" ""
                        |"" "" "" ""
                        |Peter NNP NNP B-PER
                        |Parker NNP NNP I-PER
                        |is VBZ VBZ O
                        |a DT DT O
                        |super JJ JJ O
                        |heroe NN NN O""".stripMargin.replace("\n", "" )
    assert(fileContents==testNERText)
  }

}
