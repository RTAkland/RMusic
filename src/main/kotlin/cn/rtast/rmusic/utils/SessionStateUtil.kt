package cn.rtast.rmusic.utils

import java.io.File

object SessionStateUtil {
    private val cookieFile = File("./config/rmusic/profile.json")
    fun netease(): Boolean {
        // is logged in
        if (cookieFile.exists()) {
            return true
        }
        return false
    }
}