/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */

@file:Suppress("unused")

package cn.rtast.rmusic.util

import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun ByteArray.toPNG(): ByteArray {
    val inputStream = ByteArrayInputStream(this)
    val image = ImageIO.read(inputStream)
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(image, "PNG", outputStream)
    return outputStream.toByteArray()
}

fun ByteArray.cropToCircle(): ByteArray {
    val originalImage = ImageIO.read(ByteArrayInputStream(this))
    val width = originalImage.width
    val height = originalImage.height
    val circularImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g2d = circularImage.createGraphics()
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.clip(Area(Ellipse2D.Double(0.0, 0.0, width.toDouble(), height.toDouble())))
    g2d.drawImage(originalImage, 0, 0, null)
    g2d.dispose()
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(circularImage, "PNG", outputStream)
    return outputStream.toByteArray()
}

fun BufferedImage.isFullyTransparent(): Boolean {
    val width = this.width
    val height = this.height
    for (y in 0 until height) {
        for (x in 0 until width) {
            val pixel = this.getRGB(x, y)
            if ((pixel shr 24) != 0x00) {
                return false
            }
        }
    }
    return true
}


fun BufferedImage.scaleImage(size: Pair<Int, Int>): BufferedImage {
    val scaledImage = BufferedImage(size.first, size.second, this.type)
    val graphics2d = scaledImage.createGraphics()
    graphics2d.drawImage(this, 0, 0, size.first, size.second, null)
    graphics2d.dispose()
    return scaledImage
}

fun ByteArray.toBufferedImage(): BufferedImage {
    ByteArrayInputStream(this).use { inputStream ->
        return ImageIO.read(inputStream)
    }
}

fun BufferedImage.toByteArray(): ByteArray {
    ByteArrayOutputStream().use { outputStream ->
        ImageIO.write(this, "PNG", outputStream)
        return outputStream.toByteArray()
    }
}

fun BufferedImage.createRecordImage(): BufferedImage {
    val diameter = this.width
    val borderThickness = 10
    val scaleRatio = 0.8
    val newWidth = (diameter * scaleRatio).toInt()
    val newHeight = (diameter * scaleRatio).toInt()
    val resizedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB)
    val g2dResize: Graphics2D = resizedImage.createGraphics()
    g2dResize.drawImage(this, 0, 0, newWidth, newHeight, null)
    g2dResize.dispose()
    val outputImage =
        BufferedImage(diameter + borderThickness * 2, diameter + borderThickness * 2, BufferedImage.TYPE_INT_ARGB)
    val g2d = outputImage.createGraphics()
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.color = Color(30, 30, 30)
    g2d.fillOval(0, 0, outputImage.width, outputImage.height)
    val ellipse = Ellipse2D.Double(
        borderThickness.toDouble(),
        borderThickness.toDouble(),
        diameter.toDouble(),
        diameter.toDouble()
    )
    g2d.clip(ellipse)
    val x = (outputImage.width - resizedImage.width) / 2
    val y = (outputImage.height - resizedImage.height) / 2
    g2d.drawImage(resizedImage, x, y, null)
    g2d.dispose()
    return outputImage
}