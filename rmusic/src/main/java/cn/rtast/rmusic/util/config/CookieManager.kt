/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.util.config

import cn.rtast.rmusic.entity.Cookie
import cn.rtast.rmusic.enums.MusicPlatform
import cn.rtast.rmusic.util.str.fromJson
import cn.rtast.rmusic.util.str.toJson
import java.io.File

class CookieManager {

    private val dir = File("./config/rmusic").apply { mkdirs() }
    private val file = File(dir, "cookie")
    var currentCookie: Cookie? = null

    init {
        if (!this.file.exists()) {
            this.file.createNewFile()
            val defaultEmptyCookie = Cookie("", "").toJson()
            this.file.writeText(defaultEmptyCookie)
        }
        this.currentCookie = this.file.readText().fromJson<Cookie>()
    }

    fun login(cookie: String, platform: MusicPlatform) {
        val currentCookies = this.getCookie()
        val afterCookie = when (platform) {
            MusicPlatform.QQ -> Cookie("", "")
            MusicPlatform.Netease -> Cookie(cookie, currentCookies.kugouCookie)
            MusicPlatform.KuGou -> Cookie(currentCookies.neteaseCookie, cookie)
        }
        this.write(afterCookie)
    }

    private fun write(cookie: Cookie) {
        this.file.writeText(cookie.toJson())
    }

    private fun getCookie(): Cookie {
        file.createNewFile()
        return file.readText().fromJson<Cookie>()
    }

    fun logout() {
        file.delete()
    }

    fun reload() {
        this.currentCookie = this.getCookie()
    }
}