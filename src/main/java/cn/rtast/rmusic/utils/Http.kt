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
import okhttp3.OkHttpClient
import okhttp3.Request

object Http {

    private val client = OkHttpClient()

    fun get(urlString: String, params: Map<String, String>?): String {
        val urlBuilder = StringBuilder(urlString)
        params?.let { paramMap ->
            urlBuilder.append("?")
            for ((key, value) in paramMap) {
                urlBuilder.append("$key=$value&")
            }
            val state = RMusic.loginManager.retrieveLoginCookie()
            if (state.cookie != null) {
                urlBuilder.append("cookie=${state.cookie}&")
            }
            urlBuilder.deleteCharAt(urlBuilder.length - 1)
        }
        val url = urlBuilder.toString()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            return response.body.string()
        }
    }
}
