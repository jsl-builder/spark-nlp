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

package com.johnsnowlabs.nlp.annotators.cv.util.io

import java.awt.color.ColorSpace
import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.awt.{Color, Point}
import java.io.{File, InputStream}
import javax.imageio.ImageIO

private[johnsnowlabs] object ImageIOUtils {

  def loadImage(path: String): BufferedImage = {
    ImageIO.read(new File(path))
  }

  def loadImage(file: File): BufferedImage = {
    ImageIO.read(file)
  }

  def loadImage(file: InputStream): BufferedImage = {
    ImageIO.read(file)
  }

  def convertChannelsToType(channels: Int): Int = channels match {
    case 1 => BufferedImage.TYPE_BYTE_GRAY
    case 3 => BufferedImage.TYPE_3BYTE_BGR
    case 4 => BufferedImage.TYPE_4BYTE_ABGR
    case c =>
      throw new UnsupportedOperationException(
        "Image resize: number of output  " +
          s"channels must be 1, 3, or 4, got $c.")
  }

  def byteToBufferedImage(bytes: Array[Byte], w: Int, h: Int, nChannels: Int): BufferedImage = {
    val img = new BufferedImage(w, h, convertChannelsToType(nChannels))
    img.setData(
      Raster
        .createRaster(img.getSampleModel, new DataBufferByte(bytes, bytes.length), new Point()))
    img
  }

  def BufferedImageToByte(img: BufferedImage): Array[Byte] = {

    if (img == null) {
      Array.empty[Byte]
    } else {

      val is_gray = img.getColorModel.getColorSpace.getType == ColorSpace.TYPE_GRAY
      val has_alpha = img.getColorModel.hasAlpha

      val height = img.getHeight
      val width = img.getWidth
      val (nChannels, mode) =
        if (is_gray) (1, "CV_8UC1")
        else if (has_alpha) (4, "CV_8UC4")
        else (3, "CV_8UC3")

      assert(height * width * nChannels < 1e9, "image is too large")
      val decoded = Array.ofDim[Byte](height * width * nChannels)

      // grayscale images in Java require special handling to get the correct intensity
      if (is_gray) {
        var offset = 0
        val raster = img.getRaster
        for (h <- 0 until height) {
          for (w <- 0 until width) {
            decoded(offset) = raster.getSample(w, h, 0).toByte
            offset += 1
          }
        }
      } else {
        var offset = 0
        for (h <- 0 until height) {
          for (w <- 0 until width) {
            val color = new Color(img.getRGB(w, h))

            decoded(offset) = color.getBlue.toByte
            decoded(offset + 1) = color.getGreen.toByte
            decoded(offset + 2) = color.getRed.toByte
            if (nChannels == 4) {
              decoded(offset + 3) = color.getAlpha.toByte
            }
            offset += nChannels
          }
        }
      }
      decoded
    }
  }

}
