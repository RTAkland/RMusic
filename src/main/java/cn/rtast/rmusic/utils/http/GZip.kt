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


package cn.rtast.rmusic.utils.http

import cn.rtast.rmusic.models.CompressModel
import cn.rtast.rmusic.utils.getMD5
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object GZip {

    fun compress(data: ByteArray): CompressModel {
        val outputStream = ByteArrayOutputStream()
        val gzipOutputStream = GZIPOutputStream(outputStream)
        gzipOutputStream.write(data)
        gzipOutputStream.close()

        val output = outputStream.toByteArray()
        val md5 = output.getMD5()
        return CompressModel(output, output.size.toLong(), data.size.toLong(), md5)
    }

    fun decompress(data: ByteArray): CompressModel {
        val inputStream = ByteArrayInputStream(data)
        val gzipInputStream = GZIPInputStream(inputStream)

        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (gzipInputStream.read(buffer).also { bytesRead = it } > 0) {
            outputStream.write(buffer, 0, bytesRead)
        }

        gzipInputStream.close()
        outputStream.close()

        val output = outputStream.toByteArray()

        val md5 = output.getMD5()
        return CompressModel(output, output.size.toLong(), data.size.toLong(), md5)
    }
}