/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.qq

import com.google.gson.annotations.SerializedName

data class RawQQMusicSearch(
    val response: Response
) {
    data class Response(
        val data: Data
    )

    data class Data(
        val song: Song
    )

    data class Song(
        val list: List<SongItem>
    )

    data class SongItem(
        @SerializedName("albummid")
        val albumMid: String,
        @SerializedName("albumname")
        val albumName: String,
        val singer: List<Singer>,
        @SerializedName("songmid")
        val songMid: String,
    )

    data class Singer(
        val name: String,
    )
}