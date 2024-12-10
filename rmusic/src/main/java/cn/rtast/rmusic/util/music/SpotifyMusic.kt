/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/10
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.entity.spotify.SpotifyLoginResponse
import cn.rtast.rmusic.entity.spotify.SpotifyRawSearchResult
import cn.rtast.rmusic.util.Http
import cn.rtast.rmusic.util.str.encodeToBase64
import okhttp3.FormBody

object SpotifyMusic {
    private const val API_HOST = "https://api.spotify.com/v1"
    private const val ACCOUNT_API_HOST = "https://accounts.spotify.com/api/token"
    private const val CLIENT_ID = "x"
    private const val CLIENT_SECRET = "x"
    private const val SEARCH_PATH = "search"
    private const val TRACK_PATH = "tracks"

    private fun getAccessToken(): String {
        val token = "$CLIENT_ID:$CLIENT_SECRET".encodeToBase64()
        val header = mapOf("Authorization" to "Basic $token")
        val form = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .build()
        return Http.post<SpotifyLoginResponse>(ACCOUNT_API_HOST, form, header).accessToken
    }

    fun search(keyword: String): SpotifyRawSearchResult {
        val result = Http.get<SpotifyRawSearchResult>(
            "$API_HOST/$SEARCH_PATH", mapOf(
                "q" to keyword,
                "type" to "track,album"
            ),
            headers = mapOf("Authorization" to "Bearer ${this.getAccessToken()}")
        )
        return result
    }

    fun getTrack(id: String): String {
        val result = Http.get(
            "$API_HOST/$TRACK_PATH/$id",
            headers = mapOf("Authorization" to "Bearer x")
        )
        return result
    }
}

fun main() {
    println(SpotifyMusic.getTrack("6OjZY9xeZfHhAeNLaZtBAB"))
}