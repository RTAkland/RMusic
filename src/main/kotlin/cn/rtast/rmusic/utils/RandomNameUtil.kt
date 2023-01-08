package cn.rtast.rmusic.utils

import java.util.*

class RandomNameUtil {
    fun gen(length: Int = 10): String {
        var name = ""
        val char = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"
        for (i in 0..length) {
            name += char[Random().nextInt(52)]
        }
        return name
    }
}