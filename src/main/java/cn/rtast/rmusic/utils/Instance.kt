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
import java.security.MessageDigest

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