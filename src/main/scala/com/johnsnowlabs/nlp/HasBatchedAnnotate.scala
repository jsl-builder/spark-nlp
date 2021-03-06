package com.johnsnowlabs.nlp

import org.apache.spark.ml.Model
import org.apache.spark.ml.param.IntParam
import org.apache.spark.sql.Row

trait HasBatchedAnnotate[M <: Model[M]] {

  this: RawAnnotator[M] =>

  /** Size of every batch.
    *
    * @group param
    * */
  val batchSize = new IntParam(this, "batchSize", "Size of every batch.")

  /** Size of every batch.
    *
    * @group setParam
    * */
  def setBatchSize(size: Int): this.type = {
    val recommended = $(batchSize)
    require(recommended <= 0, "batchSize must be greater than 0")
    set(this.batchSize, recommended)
  }

  /** Size of every batch.
    *
    * @group getParam
    * */
  def getBatchSize: Int = $(batchSize)

  def batchProcess(rows: Iterator[_]): Iterator[Row] = {
    rows.grouped(getBatchSize).flatMap { case batchedRows: Seq[Row] =>
      val inputAnnotations = batchedRows.map(row => {
        getInputCols.flatMap(inputCol => {
          row.getAs[Seq[Row]](inputCol).map(Annotation(_))
        })
      })
      val outputAnnotations = batchAnnotate(inputAnnotations)
      batchedRows.zip(outputAnnotations).map { case (row, annotations) =>
        row.toSeq ++ Array(annotations.map(a => Row(a.productIterator.toSeq:_*)))
      }
    }.map(Row.fromSeq)
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
