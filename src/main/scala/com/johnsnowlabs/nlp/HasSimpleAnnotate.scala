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

import org.apache.spark.ml.Model
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

trait HasSimpleAnnotate[M <: Model[M]] {

  this: AnnotatorModel[M] =>

  /**
   * takes a document and annotations and produces new annotations of this annotator's annotation type
   *
   * @param annotations Annotations that correspond to inputAnnotationCols generated by previous annotators if any
   * @return any number of annotations processed for every input annotation. Not necessary one to one relationship
   */
  def annotate(annotations: Seq[Annotation]): Seq[Annotation]

  /**
   * Wraps annotate to happen inside SparkSQL user defined functions in order to act with [[org.apache.spark.sql.Column]]
   *
   * @return udf function to be applied to [[inputCols]] using this annotator's annotate function as part of ML transformation
   */
  def dfAnnotate: UserDefinedFunction = udf { annotationProperties: Seq[AnnotationContent] =>
    annotate(annotationProperties.flatMap(_.map(Annotation(_))))
  }

}
