/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.util

import java.awt.AlphaComposite
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

private const val CIRCLE_SIZE = 128

fun ByteArray.toPNG(): ByteArray {
    val inputStream = ByteArrayInputStream(this)
    val image = ImageIO.read(inputStream)
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(image, "PNG", outputStream)
    return outputStream.toByteArray()
}

fun ByteArray.toCircle(): ByteArray {
    val origin = ImageIO.read(ByteArrayInputStream(this))
    val circleImage = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val g2d = circleImage.createGraphics()
    g2d.composite = AlphaComposite.Src
    g2d.fillRect(0, 0, size, size)
    g2d.color = Color.BLACK
    g2d.fillOval(0, 0, size, size)
    g2d.composite = AlphaComposite.SrcIn
    g2d.drawImage(origin, 0, 0, size, size, null)
    g2d.dispose()
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(circleImage, "PNG", byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}