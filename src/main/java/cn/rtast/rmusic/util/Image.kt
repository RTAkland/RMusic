/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.util

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