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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.Base64

fun String.openFile(): File {
    return File("${RMusic.DEFAULT_CONF_PATH}$this")
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
