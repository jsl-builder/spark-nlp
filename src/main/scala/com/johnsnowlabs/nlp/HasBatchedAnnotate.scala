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

package com.johnsnowlabs.nlp

import org.apache.spark.ml.Model
import org.apache.spark.ml.param.{BooleanParam, IntParam}
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, Dataset, Row}

trait HasBatchedAnnotate[M <: Model[M]] {

  this: RawAnnotator[M] =>

  /** Size of every batch (Default depends on model).
   *
   * @group param
   * */
  val batchSize = new IntParam(this, "batchSize", "Size of every batch.")

  /** Size of every batch.
   *
   * @group setParam
   * */
  def setBatchSize(size: Int): this.type = {
    val recommended = size
    require(recommended > 0, "batchSize must be greater than 0")
    set(this.batchSize, recommended)
  }

  /** Size of every batch.
   *
   * @group getParam
   * */
  def getBatchSize: Int = $(batchSize)

  def batchProcess(dataset: Dataset[_]): DataFrame = {
    if (getBatchSize > 1) {
      batchMapPartitionsProcess(dataset)
    } else batchMapProcess(dataset.toDF())
  }

  def batchMapPartitionsProcess(dataset: Dataset[_]): DataFrame = {
    val newStructType = dataset.schema.add(getOutputCol, Annotation.arrayType)
    implicit val encoder: ExpressionEncoder[Row] = RowEncoder(newStructType)
    val processedDataFrame: DataFrame = dataset.mapPartitions(partition => {
      processPartition(partition)
    })
    processedDataFrame
  }

  private def processPartition(rows: Iterator[_]): Iterator[Row] = {
    // TODO remove the @unchecked annotation and create a type to handle different subtypes
    rows.grouped(getBatchSize).flatMap { case batchedRows: Seq[Row@unchecked] =>
      val inputAnnotations: Seq[Array[Annotation]] = batchedRows.map(row => {
        getInputCols.flatMap(inputCol => {
          row.getAs[Seq[Row]](inputCol).map(Annotation(_))
        })
      })
      val outputAnnotations = batchAnnotate(inputAnnotations)
      batchedRows.zip(outputAnnotations).map { case (row, annotations) =>
        row.toSeq ++ Array(annotations.map(a => Row(a.productIterator.toSeq: _*)))
      }
    }.map(Row.fromSeq)
  }

  def batchMapProcess(dataFrame: DataFrame): DataFrame = {
    import dataFrame.sparkSession.implicits._

    val processedDataFrame = dataFrame.withColumn("monotonically_increasing_id",
      monotonically_increasing_id())

    val batchDataFrame = processedDataFrame.map{ row =>
      val inputAnnotations: Seq[Array[Annotation]] = Seq(getInputCols.flatMap(inputCol => {
        row.getAs[Seq[Row]](inputCol).map(Annotation(_))
      }))
      val outputAnnotations: Seq[Seq[Annotation]] = batchAnnotate(inputAnnotations)
      val miId = row.getAs[Long]("monotonically_increasing_id")

      (miId, outputAnnotations)
    }.toDF( "monotonically_increasing_id", "outputAnnotations")

    val expressions: Array[Column] = Array($"monotonically_increasing_id") ++
      Array($"outputAnnotations".getItem(0).alias(getOutputCol))
    processedDataFrame.join(batchDataFrame.select(expressions:_*), "monotonically_increasing_id")
      .drop("monotonically_increasing_id")
  }

  /**
   * takes a document and annotations and produces new annotations of this annotator's annotation type
   *
   * @param batchedAnnotations Annotations in batches that correspond to inputAnnotationCols generated by previous annotators if any
   * @return any number of annotations processed for every batch of input annotations. Not necessary one to one relationship
   *
   *         IMPORTANT: !MUST! return sequences of equal lengths !!
   *         IMPORTANT: !MUST! return sentences that belong to the same original row !! (challenging)
   */
  def batchAnnotate(batchedAnnotations: Seq[Array[Annotation]]): Seq[Seq[Annotation]]

}
