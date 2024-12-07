/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.qq

import com.google.gson.annotations.SerializedName

data class GetQQSongInfo(
    val response: Response
) {
    data class Response(
        @SerializedName("songinfo")
        val songInfo: SongInfo,
    )

    data class SongInfo(
        val data: Data
    )

    data class Data(
        @SerializedName("track_info")
        val trackInfo: TrackInfo
    )

    data class TrackInfo(
        val name: String,
        val singer: List<Singer>,
        val interval: Long
    )

    data class Singer(
        val name: String,
    )
}