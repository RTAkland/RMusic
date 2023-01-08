package cn.rtast.rmusic.music

import cn.rtast.rmusic.data.netease.ProfileModel
import cn.rtast.rmusic.data.netease.login.LoginResponseModel
import cn.rtast.rmusic.data.netease.search.SearchResponseModel
import cn.rtast.rmusic.data.netease.search.Song
import cn.rtast.rmusic.data.netease.url.Data
import cn.rtast.rmusic.data.netease.url.UrlModel
import com.google.gson.Gson
import java.io.File
import java.net.URL
import java.net.URLEncoder

class Music163 {
    private val gson = Gson()
    private val rootApi = "https://api.163.rtast.cn"
    private val cookie: String? = null

    fun login(email: String, password: String): Boolean {
        val result = URL("$rootApi/login?email=$email&password=$password").readText()
        val json = gson.fromJson(result, LoginResponseModel::class.java)
        if (json.code != 200) {
            return false
        }
        val file = File("./rmusic/profile.json")
        if (!file.exists()) {
            file.createNewFile()
        } else {
            file.delete()
            file.createNewFile()
        }
        val profile = ProfileModel(
            json.profile.avatarUrl,
            json.account.id,
            json.profile.backgroundUrl,
            json.cookie,
            json.profile.nickname
        )
        file.writeText(gson.toJson(profile))
        return true
    }

    fun logout(): Boolean {
        val file = File("./rmusic/cookie.json")
        return if (file.exists()) {
            file.delete()
            true
        } else {
            false
        }
    }

    fun search(keyword: String): List<Song> {
        val result = URL("$rootApi/search?keywords=${URLEncoder.encode(keyword, "UTF-8")}").readText()
        return gson.fromJson(result, SearchResponseModel::class.java).result.songs.subList(0, 10)
    }

    fun getSongUrl(id: Int): Data {
        val result = URL("$rootApi/song/url?id=$id&cookie=$cookie").readText()
        return gson.fromJson(result, UrlModel::class.java).data[0]
    }

    fun lyric(id: Int) {
        val result = URL("$rootApi/lyric/id=$id&cookie=$cookie").readText()
    }
}