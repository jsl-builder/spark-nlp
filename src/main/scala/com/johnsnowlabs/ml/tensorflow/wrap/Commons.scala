package com.johnsnowlabs.ml.tensorflow.wrap

import org.tensorflow.types.TString


case class ModelSignature(operation: String, value: String, matchingPatterns: List[String])

case class Variables(variables: Array[Byte], index: Array[Byte])

case class VariablesTfIo(variables: TString, index: TString)