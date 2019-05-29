package com.johnsnowlabs.ml.tensorflow

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.UUID

import com.johnsnowlabs.nlp.annotators.ner.dl.LoadsContrib
import com.johnsnowlabs.util.FileHelper
import org.apache.commons.io.FileUtils
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession

/**
  * Created by jose on 23/03/18.
  */
trait WriteTensorflowModel {


  def writeTensorflowModel(
                            path: String,
                            spark: SparkSession,
                            tensorflow: TensorflowWrapper,
                            suffix: String, filename:String,
                            configProtoBytes: Option[Array[Byte]] = None
                          ): Unit = {

    val uri = new java.net.URI(path.replaceAllLiterally("\\", "/"))
    val fs = FileSystem.get(uri, spark.sparkContext.hadoopConfiguration)

    // 1. Create tmp folder
    val tmpFolder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12) + suffix)
      .toAbsolutePath.toString

    val tfFile = Paths.get(tmpFolder, filename).toString

    // 2. Save Tensorflow state
    tensorflow.saveToFile(tfFile, configProtoBytes)

    // 3. Copy to dest folder
    fs.copyFromLocalFile(new Path(tfFile), new Path(path))

    // 4. Remove tmp folder
    FileUtils.deleteDirectory(new File(tmpFolder))
  }

}

trait ReadTensorflowModel {
  val tfFile: String

  def readTensorflowModel(
                           path: String,
                           spark: SparkSession,
                           suffix: String,
                           zipped:Boolean = true,
                           useBundle:Boolean = false,
                           tags:Array[String]=Array.empty
                         ): TensorflowWrapper = {

    LoadsContrib.loadContribToCluster(spark)

    val uri = new java.net.URI(path.replaceAllLiterally("\\", "/"))
    val fs = FileSystem.get(uri, spark.sparkContext.hadoopConfiguration)

    // 1. Create tmp directory
    val tmpFolder = Files.createTempDirectory(UUID.randomUUID().toString.takeRight(12)+ suffix)
      .toAbsolutePath.toString

    // 2. Copy to local dir
    fs.copyToLocalFile(new Path(path, tfFile), new Path(tmpFolder))

    // 3. Read Tensorflow state
    val tf = TensorflowWrapper.read(new Path(tmpFolder, tfFile).toString,
      zipped, tags = tags, useBundle = useBundle)

    // 4. Remove tmp folder
    FileHelper.delete(tmpFolder)

    tf
  }
}
