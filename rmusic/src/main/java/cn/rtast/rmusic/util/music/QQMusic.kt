/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.entity.SearchResult
import cn.rtast.rmusic.entity.ncm.SongDetail
import cn.rtast.rmusic.entity.qq.*
import cn.rtast.rmusic.util.Http
import cn.rtast.rmusic.util.str.LyricParser
import cn.rtast.rmusic.util.toMilliseconds

object QQMusic {

    private const val API_HOST = "https://qq-music-api.rtast.cn/"
    private const val SEARCH_PATH = "getSearchByKey"
    private const val LYRIC_PATH = "getLyric"
    private const val COVER_PATH = "getImageUrl"
    private const val SONG_INFO_PATH = "getSongInfo"
    private const val SONG_URL_PATH = "getMusicPlay"

    fun search(keyword: String): List<SearchResult> {
        return Http.get<RawQQMusicSearch>("$API_HOST/$SEARCH_PATH", mapOf("key" to keyword))
            .response.data.song.list.map {
                SearchResult(
                    it.songMid,
                    it.albumName,
                    it.singer.joinToString(",") { it.name })
            }
    }

    fun getLyric(mid: String): Map<Int, String> {
        val result = Http.get<GetSongLyric>("$API_HOST/$LYRIC_PATH", mapOf("songmid" to mid))
            .response.lyric
        return LyricParser.parseQQLyric(result)
    }

    private fun getCover(mid: String): String {
        val result = Http.get<GetQQCoverImage>("$API_HOST/$COVER_PATH", mapOf("id" to mid))
            .response.data.imageUrl
        return result
    }

    fun getSongInfo(mid: String): SongDetail {
        val result = Http.get<GetQQSongInfo>("$API_HOST/$SONG_INFO_PATH", mapOf("songmid" to mid))
            .response.songInfo.data.trackInfo
        return SongDetail(
            result.name,
            mid,
            this.getCover(mid),
            result.singer.joinToString(",") { it.name },
            result.interval.toMilliseconds()
        )
    }

    fun getSongUrl(mid: String): String {
        try {
            val result = Http.get<GetQQSongUrl>("$API_HOST/$SONG_URL_PATH", mapOf("songmid" to mid))
                .data.playUrl.values.first().url
            return result
        } catch (_: Exception) {
            return ""
        }
    }
}

fun main() {
//    println(QQMusic.search("笼"))
//    println(QQMusic.getSongInfo("000PCkUC1U48c4"))
    println(QQMusic.getSongUrl("000PCkUC1U48c4"))
}