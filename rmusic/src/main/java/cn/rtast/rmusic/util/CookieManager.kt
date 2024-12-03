/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.entity.LoginState
import cn.rtast.rmusic.util.str.fromJson
import cn.rtast.rmusic.util.str.toJson
import java.io.File

class CookieManager {

    private val dir = File("./config/rmusic").apply { mkdirs() }
    private val file = File(dir, "cookie.json")

    fun login(cookie: String) {
        val state = LoginState(cookie).toJson()
        file.delete()
        file.createNewFile()
        file.writeText(state)
    }

    fun getCookie(): String? {
        file.createNewFile()
        return try {
            file.readText().fromJson<LoginState>().cookie
        } catch (_: Exception) {
            null
        }
    }

    fun logout() {
        file.delete()
    }
}