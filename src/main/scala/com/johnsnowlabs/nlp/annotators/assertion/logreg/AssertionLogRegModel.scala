package com.johnsnowlabs.nlp.annotators.assertion.logreg

import com.johnsnowlabs.nlp.AnnotatorType.{ASSERTION, DOCUMENT}
import com.johnsnowlabs.nlp._
import com.johnsnowlabs.nlp.embeddings.{EmbeddingsReadable, WordEmbeddings}
import com.johnsnowlabs.nlp.pretrained.ResourceDownloader
import com.johnsnowlabs.nlp.serialization.{MapFeature, StructFeature}
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.ml.param.{IntParam, Param, ParamMap}
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._

import scala.collection.immutable.Map
import scala.collection.mutable

/**
  * Created by jose on 22/11/17.
  */

class AssertionLogRegModel(override val uid: String) extends RawAnnotator[AssertionLogRegModel]
  with Windowing with TransformModelSchema with HasWordEmbeddings  {

  override val tokenizer: Tokenizer = new SimpleTokenizer
  override val annotatorType: AnnotatorType = ASSERTION
  override val requiredAnnotatorTypes = Array(DOCUMENT)
  override lazy val wordVectors: Option[WordEmbeddings] = embeddings

  val beforeParam = new IntParam(this, "beforeParam", "Length of the context before the target")
  val afterParam = new IntParam(this, "afterParam", "Length of the context after the target")

  val nerCol = new Param[String](this, "startCol", "Column that contains NER annotations to be used as target token")
  val startCol = new Param[String](this, "startCol", "Column that contains the token number for the start of the target")
  val endCol = new Param[String](this, "endCol", "Column that contains the token number for the end of the target")

  var model: StructFeature[LogisticRegressionModel] = new StructFeature[LogisticRegressionModel](this, "logistic regression")
  var labelMap: MapFeature[Double, String] = new MapFeature[Double, String](this, "labels")

  override lazy val (before, after) = (getOrDefault(beforeParam), getOrDefault(afterParam))

  setDefault(
    beforeParam -> 11,
    afterParam -> 13
  )

  def this() = this(Identifiable.randomUID("ASSERTION"))

  def setBefore(before: Int): this.type = set(beforeParam, before)
  def setAfter(after: Int): this.type = set(afterParam, after)
  def setStart(start: String): this.type = set(startCol, start)
  def setEnd(end: String): this.type = set(endCol, end)

  override final def transform(dataset: Dataset[_]): DataFrame = {
    require(validate(dataset.schema), s"Missing annotators in pipeline. Make sure the following are present: " +
      s"${requiredAnnotatorTypes.mkString(", ")}")

    import dataset.sqlContext.implicits._

    val textCol = $(inputCols).head

    /* apply UDF to fix the length of each document */
    val processed = dataset.toDF.
      withColumn(textCol, extractTextUdf(col(getInputCols.head))).
      withColumn("features", {
        if (get(nerCol).isDefined) {
          applyWindowUdfNer(col(textCol), col($(nerCol)))
        } else if (get(startCol).isDefined & get(endCol).isDefined) {
          applyWindowUdf(col(textCol),
            col($(startCol)),
            col($(endCol)))
        } else {
          throw new IllegalArgumentException("Either nerCol or startCol and endCol must be provided in order to predict assertion")
        }
      })

    $$(model).transform(processed).withColumn(getOutputCol,
      packAnnotations(col(textCol), col(getOrDefault(startCol)),
        col(getOrDefault(endCol)), $"prediction"))
  }

  private def packAnnotations = udf { (text: String, s: Int, e: Int, prediction: Double) =>
    val tokens = text.split(" ").filter(_!="")

    /* convert start and end are indexes in the doc string */
    val start = tokens.slice(0, s).map(_.length).sum +
      tokens.slice(0, s).length // account for spaces
    val end = start + tokens.slice(s, e + 1).map(_.length).sum +
      tokens.slice(s, e + 1).length - 2 // account for spaces

    val annotation = Annotation("assertion", start, end, $$(labelMap)(prediction), Map())
    Seq(annotation)
  }

  def setModel(m: LogisticRegressionModel): this.type = set(model, m)

  def setLabelMap(labelMappings: Map[String, Double]): this.type = set(labelMap, labelMappings.map(_.swap))

  /* send this to common place */
  def extractTextUdf: UserDefinedFunction = udf { document:mutable.WrappedArray[GenericRowWithSchema] =>
    document.head.getString(3)
  }

  /** requirement for annotators copies */
  override def copy(extra: ParamMap): AssertionLogRegModel = defaultCopy(extra)
}

trait PretrainedAssertionLogRegModel {
  def pretrained(name: String = "as_fast_lg", language: Option[String] = Some("en"), folder: String = ResourceDownloader.publicFolder): AssertionLogRegModel =
    ResourceDownloader.downloadModel(AssertionLogRegModel, name, language, folder)
}


object AssertionLogRegModel extends EmbeddingsReadable[AssertionLogRegModel] with PretrainedAssertionLogRegModel
