/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.entity.SearchResult
import cn.rtast.rmusic.entity.qq.GetSongLyric
import cn.rtast.rmusic.entity.qq.RawQQMusicSearch
import cn.rtast.rmusic.util.Http
import cn.rtast.rmusic.util.str.LyricParser

object QQMusic {

    private val API_HOST get() = ""
    private const val SEARCH_PATH = "getSearchByKey"
    private const val LYRIC_PATH = "getLyric"

    fun search(keyword: String): List<SearchResult> {
        return Http.get<RawQQMusicSearch>("$API_HOST/$SEARCH_PATH", mapOf("key" to keyword))
            .response.data.song.list.map {
                SearchResult(
                    it.songMid,
                    it.albumName,
                    it.singer.joinToString(",") { it.name })
            }
    }

    fun getLyric(mid: String): Map<Float, String> {
        val result = Http.get<GetSongLyric>("$API_HOST/$LYRIC_PATH", mapOf("songmid" to mid))
            .response.lyric
        return LyricParser.parseQQLyric(result)
    }

    fun getCover(mid: String) {
        TODO()
    }
}