/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.music

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.data.netease.ProfileModel
import cn.rtast.rmusic.data.netease.login.LoginResponseModel
import cn.rtast.rmusic.data.netease.search.SearchResponseModel
import cn.rtast.rmusic.data.netease.search.Song
import cn.rtast.rmusic.data.netease.url.UrlModel
import cn.rtast.rmusic.utils.HTTPUtil
import com.google.gson.Gson
import java.io.File
import java.net.URLEncoder

class Music163 {
    private val gson = Gson()
    private val rootApi163 = RMusic.API_ADDR_163
    private val cookie: String? = null

    fun login(email: String, password: String): Boolean {
        val result = HTTPUtil().get("$rootApi163/login?email=$email&password=$password")
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
        val result = HTTPUtil().get("$rootApi163/search?keywords=${URLEncoder.encode(keyword, "UTF-8")}")
        val songs = gson.fromJson(result, SearchResponseModel::class.java).result.songs
        return if (songs.size >= 10) {
            gson.fromJson(result, SearchResponseModel::class.java).result.songs.subList(0, 10)
        } else {
            songs
        }
    }

    fun getSongUrl(id: Int): UrlModel {
        val result = HTTPUtil().get("$rootApi163/song/url?id=$id&cookie=$cookie")
        return gson.fromJson(result, UrlModel::class.java)
    }

}