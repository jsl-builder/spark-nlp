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

package com.johnsnowlabs.nlp.annotators.audio

import com.johnsnowlabs.nlp.AudioAssembler
import com.johnsnowlabs.nlp.annotator.Tokenizer
import com.johnsnowlabs.nlp.base.LightPipeline
import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
import com.johnsnowlabs.nlp.util.io.ResourceHelper
import com.johnsnowlabs.tags.SlowTest
import com.johnsnowlabs.util.Benchmark
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec

class Wav2Vec2ForCTCTestSpec extends AnyFlatSpec {

  val spark: SparkSession = ResourceHelper.spark
  import spark.implicits._

  val pathToFileWithFloats = "src/test/resources/audio/csv/audio_floats.csv"

  val audioAssembler: AudioAssembler = new AudioAssembler()
    .setInputCol("audio_content")
    .setOutputCol("audio_assembler")

  val processedAudioFloats: Dataset[Row] =
    spark.read
      .option("inferSchema", value = true)
      .json("src/test/resources/audio/json/audio_floats.json")
      .select($"float_array".cast("array<float>").as("audio_content"))

  processedAudioFloats.printSchema()

  "Wav2Vec2ForCTC" should "correctly transform speech to text from already processed audio files" taggedAs SlowTest in {

    val speechToText: Wav2Vec2ForCTC = Wav2Vec2ForCTC
      .pretrained()
      .setInputCols("audio_assembler")
      .setOutputCol("text")

    val pipeline: Pipeline = new Pipeline().setStages(Array(audioAssembler, speechToText))

    val bufferedSource =
      scala.io.Source.fromFile(pathToFileWithFloats)

    val rawFloats = bufferedSource
      .getLines()
      .map(_.split(",").head.trim.toFloat)
      .toArray
    bufferedSource.close

    val processedAudioFloats = Seq(rawFloats).toDF("audio_content")
    processedAudioFloats.printSchema()

    val pipelineDF = pipeline.fit(processedAudioFloats).transform(processedAudioFloats)

    Benchmark.measure(iterations = 1, forcePrint = true, description = "Time to show result") {
      pipelineDF.select("text").show(10, false)
    }

  }

  "Wav2Vec2ForCTC" should "correctly work with Tokenizer" taggedAs SlowTest in {

    val speechToText: Wav2Vec2ForCTC = Wav2Vec2ForCTC
      .pretrained()
      .setInputCols("audio_assembler")
      .setOutputCol("document")

    val token = new Tokenizer()
      .setInputCols("document")
      .setOutputCol("token")

    val pipeline: Pipeline =
      new Pipeline().setStages(Array(audioAssembler, speechToText, token))

    val bufferedSource =
      scala.io.Source.fromFile(pathToFileWithFloats)

    val rawFloats = bufferedSource
      .getLines()
      .map(_.split(",").head.trim.toFloat)
      .toArray
    bufferedSource.close

    val processedAudioFloats = Seq(rawFloats).toDF("audio_content")
    processedAudioFloats.printSchema()

    val pipelineDF = pipeline.fit(processedAudioFloats).transform(processedAudioFloats)

    pipelineDF.select("document").show(10, false)
    pipelineDF.select("token").show(10, false)

  }

  "Wav2Vec2ForCTC" should "transform speech to text with LightPipeline" taggedAs SlowTest in {
    val speechToText: Wav2Vec2ForCTC = Wav2Vec2ForCTC
      .pretrained()
      .setInputCols("audio_assembler")
      .setOutputCol("document")

    val token = new Tokenizer()
      .setInputCols("document")
      .setOutputCol("token")

    val pipeline: Pipeline =
      new Pipeline().setStages(Array(audioAssembler, speechToText, token))

    val bufferedSource =
      scala.io.Source.fromFile(pathToFileWithFloats)

    val rawFloats = bufferedSource
      .getLines()
      .map(_.split(",").head.trim.toFloat)
      .toArray
    bufferedSource.close

    val processedAudioFloats = Seq(rawFloats).toDF("audio_content")

    val pipelineModel = pipeline.fit(processedAudioFloats)
    val lightPipeline = new LightPipeline(pipelineModel)
    val result = lightPipeline.fullAnnotate(rawFloats)

    assert(result("audio_assembler").nonEmpty)
    assert(result("document").nonEmpty)
    assert(result("token").nonEmpty)
  }

  it should "transform several speeches to text with LightPipeline" taggedAs SlowTest in {
    val speechToText: Wav2Vec2ForCTC = Wav2Vec2ForCTC
      .pretrained()
      .setInputCols("audio_assembler")
      .setOutputCol("document")

    val token = new Tokenizer()
      .setInputCols("document")
      .setOutputCol("token")

    val pipeline: Pipeline =
      new Pipeline().setStages(Array(audioAssembler, speechToText, token))

    val bufferedSource =
      scala.io.Source.fromFile(pathToFileWithFloats)

    val rawFloats = bufferedSource
      .getLines()
      .map(_.split(",").head.trim.toFloat)
      .toArray
    bufferedSource.close

    val processedAudioFloats = Seq(rawFloats).toDF("audio_content")
    processedAudioFloats.printSchema()

    val pipelineModel = pipeline.fit(processedAudioFloats)
    val lightPipeline = new LightPipeline(pipelineModel)
    val results = lightPipeline.fullAnnotate(Array(rawFloats, rawFloats))

    results.foreach { result =>
      assert(result("audio_assembler").nonEmpty)
      assert(result("document").nonEmpty)
      assert(result("token").nonEmpty)
    }

  }

  "Wav2Vec2ForCTC" should "be serializable" taggedAs SlowTest in {

    val speechToText: Wav2Vec2ForCTC = Wav2Vec2ForCTC
      .pretrained()
      .setInputCols("audio_assembler")
      .setOutputCol("text")

    val pipeline: Pipeline = new Pipeline().setStages(Array(audioAssembler, speechToText))

    val pipelineModel = pipeline.fit(processedAudioFloats)
    pipelineModel.stages.last
      .asInstanceOf[Wav2Vec2ForCTC]
      .write
      .overwrite()
      .save("./tmp_wav2vec_model")

    val loadedWav2Vec2 = Wav2Vec2ForCTC.load("./tmp_wav2vec_model")
    val newPipeline: Pipeline = new Pipeline().setStages(Array(audioAssembler, loadedWav2Vec2))

    newPipeline
      .fit(processedAudioFloats)
      .transform(processedAudioFloats)
      .select("text")
      .show(10, false)

  }

  "Wav2Vec2ForCTC" should "benchmark" taggedAs SlowTest in {

    val speechToText: Wav2Vec2ForCTC = Wav2Vec2ForCTC
      .pretrained()
      .setInputCols("audio_assembler")
      .setOutputCol("text")

    val pipeline: Pipeline = new Pipeline().setStages(Array(audioAssembler, speechToText))

    Array(1, 2, 4, 8).foreach(b => {
      speechToText.setBatchSize(b)

      val pipelineModel = pipeline.fit(processedAudioFloats)
      val pipelineDF = pipelineModel.transform(processedAudioFloats)

      println(
        s"batch size: ${pipelineModel.stages.last.asInstanceOf[Wav2Vec2ForCTC].getBatchSize}")

      Benchmark.measure(
        iterations = 1,
        forcePrint = true,
        description = "Time to save pipeline") {
        pipelineDF.select("text").count()
      }
    })
  }

  "Wav2Vec2ForCTC" should "pretrained pipeline" taggedAs SlowTest in {

    val processedAudioDoubles: Dataset[Row] =
      spark.read
        .option("inferSchema", value = true)
        .json("src/test/resources/audio/json/audio_floats.json")
        .select($"float_array".as("audio_content"))

    processedAudioDoubles.printSchema()

    val pipelineModel = PretrainedPipeline("pipeline_asr_wav2vec2_base_960h")

    val pipelineDF = pipelineModel.transform(processedAudioDoubles)
    pipelineDF.show()

  }

}
