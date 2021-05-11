package com.johnsnowlabs.ml.tensorflow

import org.tensorflow.ndarray.buffer._
import org.tensorflow.ndarray.{NdArray, Shape, StdArrays}
import org.tensorflow.types.family.{TNumber, TType}
import org.tensorflow.types._
import org.tensorflow.{DataType, Operand, Tensor}
import org.tensorflow.op.Scope
import org.tensorflow.op.core.{Concat, Constant, Reshape, Reverse, Zeros}

import java.lang
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._
import scala.language.existentials

/**
  * This class is being used to initialize Tensors of different types and shapes for Tensorflow operations
  */
class TensorResources {
  private val tensors = ArrayBuffer[Tensor[_]]()

  def createTensor[T](obj: T): Tensor[_ <: TType] = {
    val result = obj match {
      case float: Float =>
        TFloat32.scalarOf(float)

      case str: String =>
        TString.scalarOf(str)

      case array: Array[String] =>
        TString.tensorOf(StdArrays.ndCopyOf(array))

      case floatArray: Array[Float] =>
        TFloat32.tensorOf(StdArrays.ndCopyOf(floatArray))

      case bidimArray: Array[Array[String]] =>
        TString.tensorOf(StdArrays.ndCopyOf(bidimArray))

      case bidimArray: Array[Array[Float]] =>
        TFloat32.tensorOf(StdArrays.ndCopyOf(bidimArray))

      case tridimArray: Array[Array[Array[Float]]] =>
        TFloat32.tensorOf(StdArrays.ndCopyOf(tridimArray))

      case array: Array[Int] =>
        TInt32.tensorOf(StdArrays.ndCopyOf(array))

      case array: Array[Array[Int]] =>
        TInt32.tensorOf(StdArrays.ndCopyOf(array))

      case array: Array[Array[Array[Int]]] =>
        TInt32.tensorOf(StdArrays.ndCopyOf(array))

      case array: Array[Array[Array[Byte]]] =>
        TUint8.tensorOf(StdArrays.ndCopyOf(array))

    }
    tensors.append(result)
    result
  }


  def createIntBufferTensor(shape: Array[Long], buf: IntDataBuffer): Tensor[TInt32] = {
    val result = TInt32.tensorOf(Shape.of(shape:_*), buf)
    tensors.append(result)
    result
  }

  def createLongBufferTensor(shape: Array[Long], buf: LongDataBuffer): Tensor[TInt64] = {
    val result = TInt64.tensorOf(Shape.of(shape:_*), buf)
    tensors.append(result)
    result
  }

  def createFloatBufferTensor(shape: Array[Long], buf: FloatDataBuffer): Tensor[TFloat32] = {
    val result = TFloat32.tensorOf(Shape.of(shape:_*), buf)
    tensors.append(result)
    result
  }

  def clearTensors(): Unit = {
    for (tensor <- tensors) {
      tensor.close()
    }

    tensors.clear()
  }

  def clearSession(outs: mutable.Buffer[Tensor[_]]): Unit = {
    outs.foreach(_.close())
  }

  def createIntBuffer(dim: Int): IntDataBuffer = {
    DataBuffers.ofInts(dim)
  }

  def createLongBuffer(dim: Int): LongDataBuffer = {
    DataBuffers.ofLongs(dim)
  }

  def createFloatBuffer(dim: Int): FloatDataBuffer = {
    DataBuffers.ofFloats(dim)
  }
}

object TensorResources {
  // TODO all these implementations are not tested

  def calculateTensorSize(source: Tensor[_], size: Option[Int]): Int = {
    size.getOrElse{
      // Calculate real size from tensor shape
      val shape = source.shape()
      shape.asArray.foldLeft(1L)(_*_).toInt
    }
  }

  def extractInts(source: Tensor[_], size: Option[Int] = None): Array[Int] = {
    val realSize = calculateTensorSize(source ,size)
    val buffer = Array.fill(realSize)(0)
    source.rawData.asInts.read(buffer)
    buffer
  }

  def extractInt(source: Tensor[_], size: Option[Int] = None): Int =
    extractInts(source).head

  def extractLongs(source: Tensor[_], size: Option[Int] = None): Array[Long] = {
    val realSize = calculateTensorSize(source ,size)
    val buffer = Array.fill(realSize)(0L)
    source.rawData.asLongs.read(buffer)
    buffer
  }

  def extractFloats(source: Tensor[_], size: Option[Int] = None): Array[Float] = {
    val realSize = calculateTensorSize(source ,size)
    val buffer = Array.fill(realSize)(0f)
    source.rawData.asFloats.read(buffer)
    buffer
  }

  def reverseTensor(scope: Scope, tensor: Operand[_], dimension: Int): Tensor[_] = {
    val axis = Constant.vectorOf(scope, Array[Int](dimension))
    val reversedTensor = Reverse.create(scope, tensor, axis).asTensor
    reversedTensor
  }

  def concatTensors[T <: TNumber](scope: Scope, tensors: Array[Operand[T]], dimension: Int):
  Tensor[_ >: TFloat32 with TInt32 <: NdArray[_ >: lang.Float with Integer] with TNumber] = {
    val axis: Operand[TInt32] = Constant.vectorOf(scope, Array[Int](dimension))
    val tensorType = tensors.head.data() match {
      case floatType: TFloat32 => Concat.create(scope, tensors.asInstanceOf[Array[Operand[TFloat32]]].toList.asJava, axis)
      case intType: TInt32 => Concat.create(scope, tensors.asInstanceOf[Array[Operand[TInt32]]].toList.asJava, axis)
    }
    tensorType.asTensor()
  }

  def reshapeTensor(scope: Scope, tensor: Operand[_ <: TType], shape: Array[Int]): Tensor[_ <: TType] = {
    val reshapedTensor = Reshape.create(scope, tensor, Constant.vectorOf(scope, shape))
    reshapedTensor.asTensor()
  }

}