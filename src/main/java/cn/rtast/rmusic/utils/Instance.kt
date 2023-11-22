/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package cn.rtast.rmusic.utils

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.utils.http.Params
import com.google.common.reflect.TypeToken
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun ByteArray.getMD5(): String {
    val ins = MessageDigest.getInstance("MD5")
    return ins.digest(this).joinToString("") { "%02x".format(it) }
}

fun joinToURL(url: String, params: Params): String {
    val newParams = Params.Builder().apply {
        params.params.forEach { (key, value) ->
            this.addParam(key, value)
        }
        if (SessionManager.cookie != null) {
            this.addParam("cookie", SessionManager.cookie!!)
        }
    }.build()

    val newUrl = StringBuilder(url)
    val paramsString = newParams.toString()
    val paramsToAdd = if (url.contains("?")) paramsString.removePrefix("?") else paramsString
    newUrl.append(paramsToAdd)
    return newUrl.toString()
}


inline fun <reified T> String.fromArrayJson(): T {
    return RMusic.gson.fromJson(this, object : TypeToken<T>() {}.type)
}

fun Any.toJson(): String {
    return RMusic.gson.toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return RMusic.gson.fromJson(this, T::class.java)
}

fun compress(input: String): ByteArray {

    val outputStream = ByteArrayOutputStream()
    val gzip = GZIPOutputStream(outputStream)
    gzip.write(input.toByteArray())
    gzip.close()
    return outputStream.toByteArray()
}

fun decompress(compressed: ByteArray): String {
    val inputStream = ByteArrayInputStream(compressed)
    val outputStream = ByteArrayOutputStream()
    val input = GZIPInputStream(inputStream)
    val buffer = ByteArray(1024)
    var len: Int
    while (input.read(buffer).also { len = it } > 0) {
        outputStream.write(buffer, 0, len)
    }
    input.close()
    outputStream.close()
    return outputStream.toString("UTF-8")
}

fun sendPacket(message: String, player: ServerPlayerEntity) {
    val compressed = compress(message)
    val packet = PacketByteBufs.create()
    packet.writeByteArray(compressed)
    ServerPlayNetworking.send(player, RMusic.RNetworkChannel, packet)
}

fun base64ToByteArray(base64Image: String): ByteArray {
    val startIndex = base64Image.indexOf("base64,") + 7
    val base64EncodedImage = base64Image.substring(startIndex)
    return Base64.getDecoder().decode(base64EncodedImage)
}

fun byteArrayTo2DArray(bytes: ByteArray, width: Int): Array<IntArray> {
    val byteArray = bytes.map { it.toInt() and 0xFF }.toIntArray()
    val height = byteArray.size / width
    val result = Array(height) { IntArray(width) }

    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i][j] = byteArray[i * width + j]
        }
    }
    return result
}