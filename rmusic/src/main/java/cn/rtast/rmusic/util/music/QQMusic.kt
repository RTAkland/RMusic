/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.RMusicClient
import cn.rtast.rmusic.entity.SearchResult
import cn.rtast.rmusic.entity.qq.*
import cn.rtast.rmusic.util.Http
import cn.rtast.rmusic.util.str.LyricParser
import java.io.FileNotFoundException
import java.net.URI

object QQMusic {

    private val API_HOST get() = RMusicClient.configManager.read().qqMusicApi
    private const val SEARCH_PATH = "getSearchByKey"
    private const val LYRIC_PATH = "getLyric"
    private const val ALBUM_IMAGE_PATH = "getImageUrl"
    private const val SONG_URL_PATH = "getMusicPlay"
    private const val ALBUM_INFO_PATH = "getAlbumInfo"

    fun search(keyword: String): List<SearchResult> {
        return Http.get<RawQQMusicSearch>("$API_HOST/$SEARCH_PATH", mapOf("key" to keyword))
            .response.data.song.list.map { map ->
                SearchResult(
                    map.songMid,
                    map.albumName,
                    map.singer.joinToString(",") { it.name })
            }
    }

    fun getLyric(mid: String): Map<Float, String> {
        val result = Http.get<GetSongLyric>("$API_HOST/$LYRIC_PATH", mapOf("songmid" to mid))
            .response.lyric
        return LyricParser.parseQQLyric(result)
    }

    fun getCover(mid: String): ByteArray? {
        val url = Http.get<GetAlbumImage>("$API_HOST/$ALBUM_IMAGE_PATH", mapOf("id" to mid))
            .response.data.imageUrl
        return try {
            URI(url).toURL().readBytes()
        } catch (_: FileNotFoundException) {
            null
        }
    }

    fun getSongUrl(mid: String): String? {
        val result = Http.get<QQGetSongUrl>(
            "$API_HOST/$SONG_URL_PATH",
            mapOf("songmid" to mid)
        ).data.playUrl
        return if (result.containsKey("undefined")) {
            null
        } else {
            result.values.first().url
        }
    }
}