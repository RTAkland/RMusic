/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.RMusicServer
import cn.rtast.rmusic.entity.SearchResult
import cn.rtast.rmusic.entity.kugou.*
import cn.rtast.rmusic.entity.ncm.SongDetail
import cn.rtast.rmusic.util.Http
import cn.rtast.rmusic.util.str.decodeToByteArray

object KuGouMusic {

    private val API_PATH = "https://kugou.rtast.cn"
    private const val QRCODE_KEY_PATH = "login/qr/key"
    private const val CHECK_QRCODE_PATH = "login/qr/check"
    private const val SEARCH_PATH = "search"
    private const val SONG_COVER_PATH = "images"
    private const val SONG_INFO_PATH = "audio"
    private const val SONG_URL_PATH = "song/url"
    private const val SONG_LYRIC_PATH = "lyric"
    private const val LYRIC_SEARCH_PATH = "search/lyric"

    private inline fun <reified T> get(endpoint: String, params: MutableMap<String, Any> = mutableMapOf()): T {
        return Http.get<T>(
            endpoint,
            params.apply {
                put("cookie", RMusicServer.cookieManager.currentCookie!!.kugouCookie)
            })
    }

    private fun getCover(hash: String): String? {
        return try {
            this.get<GetKuGouImage>("$API_PATH/$SONG_COVER_PATH", mutableMapOf("hash" to hash))
                .data.first().album.first().sizableAvatar.replace("{size}", "128")
        } catch (e: Exception) {
            null
        }
    }

    private fun lyricSearch(hash: String): Pair<String, String> {
        val result = this.get<KuGouLyricSearch>(
            "$API_PATH/$LYRIC_SEARCH_PATH", mutableMapOf(
                "hash" to hash
            )
        ).candidates
        return result.id to result.accessKey
    }

    fun search(keyword: String): List<SearchResult> {
        return this.get<RawKuGouSearch>(
            "$API_PATH/$SEARCH_PATH", mutableMapOf(
                "keywords" to keyword
            )
        ).data.lists.map {
            SearchResult(
                it.fileHash,
                it.songName,
                it.singerName
            )
        }
    }

    fun loginByQRCode(): Pair<String, ByteArray> {
        val result = this.get<GetKuGouQRCode>("$API_PATH/$QRCODE_KEY_PATH")
            .data
        return result.qrcode to result.qrcodeImg.replace("data:image/png;base64,", "").decodeToByteArray()
    }

    fun checkQRCodeStatus(key: String): Pair<String, String?>? {
        val result = this.get<KuGouLogin>("$API_PATH/$CHECK_QRCODE_PATH", mutableMapOf("key" to key))
        return if (result.status == 4) result.data.token to result.data.nickname else null
    }

    fun getSongInfo(hash: String): SongDetail {
        val result = this.get<KuGouSongInfo>(
            "$API_PATH/$SONG_INFO_PATH", mutableMapOf(
                "hash" to hash
            )
        ).data.first()
        val name = result.audioName.split(" - ").first()
        val artist = result.audioName.split(" - ").last()
        return SongDetail(
            name,
            result.hash,
            this.getCover(result.hash) ?: "",
            artist,
            result.timeLength.toLong()
        )
    }

    fun getSongUrl(hash: String): String {
        return this.get<KuGouSongUrl>("$API_PATH/$SONG_URL_PATH", mutableMapOf("hash" to hash)).backupUrl.first()
    }

    /**
     * Not support
     */
    fun getLyric(hash: String): Map<Int, String> {
        val (id, accessKey) = this.lyricSearch(hash)
        val lyric = this.get<KuGouGetLyric>(
            "$API_PATH/$SONG_LYRIC_PATH", mutableMapOf(
                "id" to id,
                "accesskey" to accessKey
            )
        ).decodeContent
        return mapOf()
    }
}