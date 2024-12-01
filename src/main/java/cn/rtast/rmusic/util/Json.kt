/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */

@file:Suppress("unused")

package cn.rtast.rmusic.util

import cn.rtast.rmusic.gson
import com.google.gson.reflect.TypeToken

fun Any.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return gson.fromJson(this, T::class.java)
}

inline fun <reified T> String.fromArrayJson(): T {
    return gson.fromJson(this, object : TypeToken<T>() {}.type)
}
