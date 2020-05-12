package com.johnsnowlabs.nlp.annotators.spell

import com.johnsnowlabs.nlp.{AnnotatorBuilder, SparkAccessor}
import com.johnsnowlabs.nlp.annotator._
import com.johnsnowlabs.nlp.annotators.spell.context.ContextSpellCheckerModel
import com.johnsnowlabs.nlp.annotators.spell.norvig.NorvigSweetingModel
import com.johnsnowlabs.nlp.annotators.spell.symmetric.SymmetricDeleteApproach
import com.johnsnowlabs.nlp.base._
import com.johnsnowlabs.nlp.util.io.ResourceHelper
import com.johnsnowlabs.util.{Benchmark, PipelineModels}
import org.scalatest._

//ResourceHelper.spark
import ResourceHelper.spark.implicits._

class SpellCheckersPerfTest extends FlatSpec {

  val documentAssembler = new DocumentAssembler().
    setInputCol("text").
    setOutputCol("document")

  val tokenizer = new Tokenizer().
    setInputCols(Array("document")).
    setOutputCol("token")

  val finisher = new Finisher().
    setInputCols("token", "spell")

  val emptyDataSet = PipelineModels.dummyDataset
  val corpusDataSetInit = AnnotatorBuilder.getTrainingDataSet("src/test/resources/spell/sherlockholmes.txt")
  val corpusDataSet = corpusDataSetInit.as[String].collect()

  System.gc()
  "Norvig pipeline" should "be fast" in {

    val spell = NorvigSweetingModel.pretrained().
      setInputCols("token").
      setOutputCol("spell").
      setDoubleVariants(true)

    val recursivePipeline = new RecursivePipeline().
      setStages(Array(
        documentAssembler,
        tokenizer,
        spell,
        finisher
      ))

    val spellmodel = recursivePipeline.fit(emptyDataSet)
    val spellplight = new LightPipeline(spellmodel)

    Benchmark.time("Light annotate norvig spell") {
      spellplight.annotate(corpusDataSet)
    }

//    val t1 = Benchmark.measure("Light annotate norvig spell results") {
//      spellplight.annotate(corpusDataSet)
//    }
//
//    print(t1)
  }

  "Symm pipeline" should "be fast" in {

    val spell = new SymmetricDeleteApproach()
      .setInputCols("token")
      .setOutputCol("spell")

    val recursivePipeline = new RecursivePipeline().
      setStages(Array(
        documentAssembler,
        tokenizer,
        spell,
        finisher
      ))

    val spellmodel = recursivePipeline.fit(corpusDataSetInit)
    val spellplight = new LightPipeline(spellmodel)

    Benchmark.time("Light annotate symmetric spell") {
      spellplight.annotate(corpusDataSet)
    }

//    val t1: Double = Benchmark.measure("Light annotate symmetric spell results") {
//      spellplight.annotate(corpusDataSet)
//    }
//
//    print(t1)

  }

  "Context pipeline" should "be fast" in {

    val spell = ContextSpellCheckerModel
      .pretrained()
      .setTradeOff(12.0f)
      .setErrorThreshold(14f)
      .setInputCols("token")
      .setOutputCol("spell")

    val recursivePipeline = new RecursivePipeline().
      setStages(Array(
        documentAssembler,
        tokenizer,
        spell,
        finisher
      ))

    val spellmodel = recursivePipeline.fit(Seq.empty[String].toDF("text"))
    val spellplight = new LightPipeline(spellmodel)

    Benchmark.time("Light annotate context spell") {
      spellplight.annotate(corpusDataSet)
    }

//    val t1: Double = Benchmark.measure("Light annotate context spell results") {
//      spellplight.annotate(corpusDataSet)
//    }
//
//    print(t1)

  }

}

