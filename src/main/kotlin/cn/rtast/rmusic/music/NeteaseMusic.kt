package cn.rtast.rmusic.music

import cn.rtast.rmusic.models.netease.SearchResultModel
import cn.rtast.rmusic.models.netease.SongUrlModel
import com.google.gson.Gson
import java.net.URL

class NeteaseMusic {

    private val api = "https://netease.api.rtast.cn/"
    private val gson = Gson()

    fun search(keyword: String): SearchResultModel {
        val rKeyword = keyword.replace(" ", "%20")
        val data = URL(api + "search/?keywords=$rKeyword").readText()
        return gson.fromJson(data, SearchResultModel::class.java)
    }

    fun getMusicURL(id: String): SongUrlModel {
        val data = URL(api + "song/url/?id=$id").readText()
        return gson.fromJson(data, SongUrlModel::class.java)
    }
}