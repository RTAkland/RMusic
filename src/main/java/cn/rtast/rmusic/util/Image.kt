/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.util

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