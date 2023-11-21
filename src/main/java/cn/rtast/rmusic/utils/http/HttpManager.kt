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

import cn.rtast.rmusic.utils.joinToURL
import okhttp3.*
import java.time.Duration

object HttpManager {

    private val okClient = OkHttpClient.Builder()
        .readTimeout(Duration.ofSeconds(5L))
        .writeTimeout(Duration.ofSeconds(5L))
        .followSslRedirects(true)
        .build()

    private fun executeRequest(request: Request.Builder): Response {
        return okClient.newCall(request.build()).execute()
    }

    fun get(url: String, params: Params?, headers: Headers?): Response {
        var newUrl = url
        if (params != null) {
            joinToURL(url, params).also { newUrl = it }
        }
        val request = Request.Builder().url(newUrl)
        if (headers != null) {
            request.headers(headers)
        }
        return executeRequest(request)
    }

    fun post(url: String, formBody: FormBody, params: Params?, headers: Headers?): Response {
        var newUrl = url
        if (params != null) {
            newUrl = joinToURL(url, params)
        }
        val request = Request.Builder().url(newUrl).post(formBody)
        if (headers != null) {
            request.headers(headers)
        }
        return executeRequest(request)
    }

}