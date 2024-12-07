/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.RMusicServer
import cn.rtast.rmusic.entity.SearchResult
import cn.rtast.rmusic.entity.ncm.*
import cn.rtast.rmusic.util.Http
import cn.rtast.rmusic.util.str.LyricParser
import cn.rtast.rmusic.util.str.decodeToByteArray


object NCMusic {
    private const val QRCODE_KEY_PATH = "login/qr/key"
    private const val CREATE_QRCODE_PATH = "login/qr/create"
    private const val CHECK_QRCODE_PATH = "login/qr/check"
    private const val SEARCH_PATH = "search"
    private const val GET_SONG_URL_PATH = "song/url"
    private const val GET_LYRIC_PATH = "lyric"
    private const val DETAIL_PATH = "song/detail"
    private const val USER_ACCOUNT_PATH = "user/account"
    private val NCM_API get() = RMusicServer.configManager.config!!.neteaseMusicAPI

    fun loginByQRCode(): Pair<String, ByteArray> {
        val key = Http.get<GetQRKey>("$NCM_API/$QRCODE_KEY_PATH").data.uniKey
        val qrcode = Http.get<CreateQRCodeImage>(
            "$NCM_API/$CREATE_QRCODE_PATH",
            mapOf("key" to key, "qrimg" to "1")
        ).data.base64Image.replace("data:image/png;base64,", "").decodeToByteArray()
        return key to qrcode
    }

    fun checkQRCodeStatus(key: String): String? {
        val result = Http.get<CheckQRCodeStatus>("$NCM_API/$CHECK_QRCODE_PATH?key=$key")
        return if (result.code == 803) result.cookie else null
    }

    fun search(keyword: String): List<SearchResult> {
        val result = Http.get<RawSearchResult>("$NCM_API/$SEARCH_PATH", mapOf("keywords" to keyword))
        return result.result.songs.map {
            SearchResult(
                it.id.toString(),
                it.name,
                it.artists.joinToString(",") { artist -> artist.name },
            )
        }
    }

    fun getSongUrl(id: Long): String {
        return Http.get<GetSongUrl>("$NCM_API/$GET_SONG_URL_PATH", mapOf("id" to id)).data.first().url
    }

    fun getLyric(id: Long): MutableMap<Int, String> {
        val lyric = Http.get<GetLyric>("$NCM_API/$GET_LYRIC_PATH", mapOf("id" to id)).lrc.lyric
        return LyricParser.parse(lyric)
    }

    fun getSongDetail(id: Long): SongDetail {
        val result = Http.get<GetSongDetail>("$NCM_API/$DETAIL_PATH", mapOf("ids" to id)).songs.first()
        return SongDetail(
            result.name,
            result.id.toString(),
            result.al.cover,
            result.ar.joinToString(", ") { it.name },
            result.dt
        )
    }

    fun getUserAccount(): String {
        return Http.get<GetUserAccount>("$NCM_API/$USER_ACCOUNT_PATH").account.profile.nickname
    }
}