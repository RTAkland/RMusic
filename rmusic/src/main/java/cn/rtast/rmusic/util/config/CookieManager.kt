/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.util.config

import cn.rtast.rmusic.entity.Cookie
import cn.rtast.rmusic.util.str.fromJson
import cn.rtast.rmusic.util.str.toJson
import java.io.File

class CookieManager {

    private val dir = File("./config/rmusic").apply { mkdirs() }
    private val file = File(dir, "cookie")
    var currentCookie: String? = null

    init {
        if (!this.file.exists()) {
            this.file.createNewFile()
            val defaultEmptyCookie = Cookie("").toJson()
            this.file.writeText(defaultEmptyCookie)
            this.currentCookie = this.file.readText().fromJson<Cookie>().cookie
        }
    }

    fun login(cookie: String) {
        val state = Cookie(cookie).toJson()
        file.delete()
        file.createNewFile()
        file.writeText(state)
    }

    private fun getCookie(): String {
        file.createNewFile()
        return file.readText().fromJson<Cookie>().cookie
    }

    fun logout() {
        file.delete()
    }

    fun reload() {
        this.currentCookie = this.getCookie()
    }
}