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
import cn.rtast.rmusic.models.Minute
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import java.io.File

fun String.openFile(): File {
    val tempFile = File(RMusic.DEFAULT_CONF_PATH)
    if (!tempFile.exists()) {
        tempFile.mkdirs()
    }
    return File("${RMusic.DEFAULT_CONF_PATH}$this")
}

fun String.removeLast2(): String {
    return this.substring(0, this.length)
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson(this, T::class.java)
}

inline fun <reified T> String.fromArrayJson(): T {
    return Gson().fromJson(this, object : TypeToken<T>() {}.type)
}

fun Int.toMinutes(): Minute {
    return Minute(this / 60, this % 60)
}

fun String.toBitmap(): Array<IntArray> {
    val bitMatrix = MultiFormatWriter().encode(
        this.toByteArray(Charsets.UTF_8).toString(), BarcodeFormat.QR_CODE, 200, 200
    )
    val qrCodeArray = Array(bitMatrix.height) { IntArray(bitMatrix.width) }
    for (x in 0 until bitMatrix.height) {
        for (y in 0 until bitMatrix.width) {
            qrCodeArray[y][x] = if (bitMatrix[x, y]) 1 else 0
        }
    }
    return qrCodeArray
}