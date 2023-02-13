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

package com.johnsnowlabs.nlp

import com.johnsnowlabs.nlp.annotators.param.ExternalResourceParam
import com.johnsnowlabs.nlp.util.io.{ExternalResource, ReadAs, ResourceHelper}
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}

import scala.collection.mutable.ArrayBuffer

class SpacyToAnnotation(override val uid: String) extends Transformer {

  def this() = this(Identifiable.randomUID("sequence_to_annotation"))

  protected[nlp] val jsonInput = new ExternalResourceParam(
    this,
    "jsonInput",
    "JSON input, it can takes in sequences of text, either tokens or sentences")

  protected[nlp] val outputAnnotatorType =
    new Param[String](this, "outputAnnotatorType", "Output annotator type")

  def setJsonInput(path: String): this.type = {
    set(
      jsonInput,
      ExternalResource(path, ReadAs.SPARK, Map("multiline" -> "true", "format" -> "JSON")))
  }

  def setOutputAnnotatorType(value: String): this.type = {
    set(outputAnnotatorType, value)
  }

  setDefault(jsonInput -> ExternalResource(path = "", ReadAs.SPARK, Map("format" -> "JSON")))

  override def transform(dataset: Dataset[_]): DataFrame = {

    require($(outputAnnotatorType).nonEmpty, "outputAnnotatorType is mandatory")

    val availableAnnotatorTypes = Array(AnnotatorType.DOCUMENT, AnnotatorType.TOKEN)
    if (!availableAnnotatorTypes.contains($(outputAnnotatorType).toLowerCase)) {
      throw new UnsupportedOperationException(
        s"Cannot convert Annotator Type: ${$(outputAnnotatorType)}. Not yet supported")
    }

    var inputDataset = dataset

    if (dataset.isEmpty || ResourceHelper.validFile($(jsonInput).path)) {
      inputDataset = ResourceHelper.readSparkDataFrame($(jsonInput))
    }

    validateSchema(inputDataset)

    if (inputDataset.schema.fieldNames.toSet.contains("sentence_ends")) {
      val annotationDataset = inputDataset.withColumn(
        "annotations",
        buildTokenAnnotationsWithSentences(
          col("tokens"),
          col("token_spaces"),
          col("sentence_ends")))

      annotationDataset
        .select("annotations.*")
        .withColumnRenamed("_1", "document")
        .withColumnRenamed("_2", "sentence")
        .withColumnRenamed("_3", "token")
    } else {
      val annotationDataset = inputDataset.withColumn(
        "annotations",
        buildTokenAnnotationsWithoutSentences(col("tokens"), col("token_spaces")))

      annotationDataset
        .select("annotations.*")
        .withColumnRenamed("_1", "document")
        .withColumnRenamed("_2", "token")
    }

  }

  private def validateSchema(dataset: Dataset[_]): Unit = {
    val expectedSchema = StructType(
      Array(
        StructField("tokens", ArrayType(StringType, false), false),
        StructField("token_spaces", ArrayType(BooleanType, false), false)))

    val expectedFieldNames = expectedSchema.fieldNames.toSet

    val actualFieldNames =
      dataset.schema.fieldNames.toSet.filter(fieldName => fieldName != "sentence_ends")
    if (actualFieldNames != expectedFieldNames) {
      throw new IllegalArgumentException(
        s"Schema validation failed. Expected field names: ${expectedFieldNames.mkString(
            ", ")}, actual field names: ${actualFieldNames.mkString(", ")}")
    }
  }

  override def copy(extra: ParamMap): Transformer = super.defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    Annotation.dataType
  }

  private def buildTokenAnnotationsWithSentences: UserDefinedFunction =
    udf((tokens: Seq[String], tokenSpaces: Seq[Boolean], sentenceEnds: Seq[Long]) => {
      val stringBuilder = new StringBuilder
      val sentences = ArrayBuffer[String]()
      val sentencesAnnotations = ArrayBuffer[Annotation]()
      val tokenAnnotations = ArrayBuffer[Annotation]()
      var beginToken = 0
      var beginSentence = 0
      var sentenceIndex = 0

      tokens.zip(tokenSpaces).zipWithIndex.foreach { case ((token, tokenSpace), index) =>
        stringBuilder.append(token)
        val endToken = beginToken + token.length - 1
        tokenAnnotations += Annotation(
          AnnotatorType.TOKEN,
          beginToken,
          endToken,
          token,
          Map("sentence" -> sentenceIndex.toString))
        if (tokenSpace) {
          beginToken = beginToken + token.length + 1
          stringBuilder.append(" ")
        } else {
          beginToken = beginToken + token.length
        }
        if (sentenceEnds.contains(index)) {
          sentences += stringBuilder.toString
          val endSentence = beginSentence + sentences.last.trim.length - 1
          sentencesAnnotations += Annotation(
            AnnotatorType.DOCUMENT,
            beginSentence,
            endSentence,
            sentences.last.trim,
            Map("sentence" -> sentenceIndex.toString))
          beginSentence = beginSentence + sentences.last.trim.length + 1
          sentenceIndex = sentenceIndex + 1
          stringBuilder.clear()
        }
      }

      val result = sentencesAnnotations.map(annotation => annotation.result).mkString(" ")
      val documentAnnotation =
        Array(Annotation(AnnotatorType.DOCUMENT, 0, result.length - 1, result, Map()))

      (documentAnnotation, sentencesAnnotations.toArray, tokenAnnotations.toArray)
    })

  private def buildTokenAnnotationsWithoutSentences: UserDefinedFunction =
    udf((tokens: Seq[String], tokenSpaces: Seq[Boolean]) => {
      val stringBuilder = new StringBuilder
      val tokenAnnotations = ArrayBuffer[Annotation]()
      var beginToken = 0

      tokens.zip(tokenSpaces).foreach { case (token, tokenSpace) =>
        stringBuilder.append(token)
        val endToken = beginToken + token.length - 1
        tokenAnnotations += Annotation(
          AnnotatorType.TOKEN,
          beginToken,
          endToken,
          token,
          Map("sentence" -> "0"))
        if (tokenSpace) {
          beginToken = beginToken + token.length + 1
          stringBuilder.append(" ")
        } else {
          beginToken = beginToken + token.length
        }
      }

      val result = stringBuilder.toString()
      val documentAnnotation =
        Array(Annotation(AnnotatorType.DOCUMENT, 0, result.length - 1, result, Map()))

      (documentAnnotation, tokenAnnotations.toArray)
    })

}
