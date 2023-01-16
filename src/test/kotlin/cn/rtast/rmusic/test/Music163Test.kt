/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/16 16:47
 */

package cn.rtast.rmusic.test

import cn.rtast.rmusic.api.Music163
import cn.rtast.rmusic.models.netease.search.SearchRespModel
import cn.rtast.rmusic.models.netease.song.SongUrlModel
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import junit.framework.Assert.*
import org.junit.jupiter.api.assertDoesNotThrow
import java.net.URL

class Music163Test {

    private val gson = Gson()
    private val rootUrl = "https://api.163.rtast.cn"

    @Test
    fun testSearch() {
        val result = URL("$rootUrl/search?keywords=恶臭的野兽先辈").readText()
        val json = gson.fromJson(result, SearchRespModel::class.java)
        assertNotNull(json)
    }

    @Test
    fun testGetSongUrl() {
        val result = URL("$rootUrl/song/url?id=114514").readText()
        val json = gson.fromJson(result, SongUrlModel::class.java)
        assertNotNull(json)
    }

    @Test
    fun testGetCookie() {
        val cookie = Music163().getCookie("e", "p")
        assertNull(cookie)
    }

    @Test
    fun testWriteCookie() {
        assertDoesNotThrow {
            val cookie = Utils().genRandom(100)
            Music163().writeCookie(cookie)
        }
    }

    @Test
    fun testLogout() {
        val status = Music163().logout()
        assertTrue(status)
    }

    @Test
    fun testLogin() {
        val email = Utils().genEmail()
        val password = Utils().genRandom(16)
        val code = Music163().login(email, password)
        assertFalse(code)
    }
}