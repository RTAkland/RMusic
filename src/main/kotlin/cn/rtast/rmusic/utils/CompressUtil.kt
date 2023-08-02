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

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object CompressUtil {
    fun compressString(input: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val gzipOutputStream = GZIPOutputStream(outputStream)
        gzipOutputStream.write(input.toByteArray(Charsets.UTF_8))
        gzipOutputStream.close()
        return outputStream.toByteArray()
    }

    fun decompressString(input: ByteArray): String {
        val inputStream = ByteArrayInputStream(input)
        val gzipInputStream = GZIPInputStream(inputStream)
        val buffer = ByteArray(1024)
        var bytesRead = gzipInputStream.read(buffer)
        val output = StringBuilder()
        while (bytesRead > 0) {
            output.append(String(buffer, 0, bytesRead, Charsets.UTF_8))
            bytesRead = gzipInputStream.read(buffer)
        }
        return output.toString()
    }
}