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

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

object HttpUtil {
    private val client = OkHttpClient()

    fun get(url: String, params: Map<String, String> = emptyMap()): String {
        val request = Request.Builder()
            .url(buildUrlWithParams(url, params))
            .build()
        return executeRequest(request)
    }

    fun post(url: String, params: Map<String, String> = emptyMap()): String {
        val requestBody = buildRequestBody(params)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        return executeRequest(request)
    }

    private fun executeRequest(request: Request): String {
        val response = client.newCall(request).execute()
        return response.body.string()
    }

    private fun buildUrlWithParams(url: String, params: Map<String, String>): String {
        val stringBuilder = StringBuilder(url)
        if (params.isNotEmpty()) {
            stringBuilder.append("?")
            params.forEach { (key, value) ->
                stringBuilder.append(key).append("=").append(value).append("&")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }
        return stringBuilder.toString()
    }

    private fun buildRequestBody(params: Map<String, String>): RequestBody {
        val formBodyBuilder = FormBody.Builder()
        params.forEach { (key, value) ->
            formBodyBuilder.add(key, value)
        }
        return formBodyBuilder.build()
    }
}
