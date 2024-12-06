/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/6
 */


package cn.rtast.rmusic.util

fun Long.toMinuteSecond(): String {
    val minutes = this / 60000
    val seconds = (this % 60000) / 1000
    return "${minutes}分${seconds}秒"
}

fun main() {
    println(280762L.toMinuteSecond())
}