/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.api

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.models.CommonSongUrl
import cn.rtast.rmusic.models.netease.detail.DetailModel
import cn.rtast.rmusic.models.netease.login.LoginRespModel
import cn.rtast.rmusic.models.netease.search.SearchRespModel
import cn.rtast.rmusic.models.netease.song.SongUrlModel
import com.google.gson.Gson
import java.io.File
import java.net.URL
import java.net.URLEncoder

open class Music163 {
    private val gson = Gson()
    private val rootApi163 = RMusic.API_URL_163
    private val cookie: String? = null

    open fun login(email: String, password: String): Boolean {
        val result = URL("$rootApi163/login?email=$email&password=$password").readText()
        val json = gson.fromJson(result, LoginRespModel::class.java)
        if (json.code != 200) {
            return false
        }
        val file = File("./config/rmusic/cookie.json")
        if (!file.exists()) {
            file.createNewFile()
        } else {
            file.delete()
            file.createNewFile()
        }
        file.writeText(json.cookie)
        return true
    }

    open fun logout(): Boolean {
        val file = File("./config/rmusic/cookie.json")
        val status: Boolean = if (file.exists()) {
            file.delete()
            true
        } else {
            false
        }
        return status
    }

    open fun search(keyword: String): MutableList<String> {
        val result = URL("$rootApi163/search?keywords=${URLEncoder.encode(keyword, "UTF-8")}").readText()
        var songs = gson.fromJson(result, SearchRespModel::class.java).result.songs
        songs = if (songs.size <= 10) {
            songs.subList(0, songs.size)
        } else {
            songs.subList(0, 10)
        }
        val lst = mutableListOf<String>()
        songs.forEach { s ->
            var artists = ""
            s.artists.forEach { ar ->
                artists += ar.name + " "
            }
            artists = artists.substring(0, artists.length - 1) // 去除最后一个歌曲作者名字后的空格
            lst.add("${s.name}^$artists^${s.id}")
        }
        return lst
    }

    open fun getSongUrl(id: Int): CommonSongUrl {
        var searchApi = "$rootApi163/song/url?id=$id"
        if (cookie != null) {
            searchApi += "&cookie=${URLEncoder.encode(cookie, "UTF-8")}"
        }
        val result = URL(searchApi).readText()
        val json = gson.fromJson(result, SongUrlModel::class.java)
        return CommonSongUrl(json.data[0].url, songName(id))
    }

    open fun songName(id: Int): String {
        val result = URL("$rootApi163/song/detail?ids=$id").readText()
        val json = gson.fromJson(result, DetailModel::class.java)
        return json.songs[0].name
    }
}