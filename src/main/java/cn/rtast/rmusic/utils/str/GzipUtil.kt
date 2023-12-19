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


package cn.rtast.rmusic.utils.str

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


object GzipUtil {

    fun compressStringToByteArray(input: String): ByteArray {
        val byteStream = ByteArrayOutputStream()
        val gzipStream = GZIPOutputStream(byteStream)

        gzipStream.write(input.toByteArray(Charsets.UTF_8))
        gzipStream.close()

        return byteStream.toByteArray()
    }

    fun decompressByteArrayToString(input: ByteArray): String {
        val gzipStream = GZIPInputStream(ByteArrayInputStream(input))
        val reader = InputStreamReader(gzipStream, Charsets.UTF_8)
        val bufferedReader = reader.buffered()
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        gzipStream.close()
        return stringBuilder.toString()
    }
}
