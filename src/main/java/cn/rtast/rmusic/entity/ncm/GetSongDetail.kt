/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.entity.ncm

import com.google.gson.annotations.SerializedName

data class GetSongDetail(
    val songs: List<Song>
) {
    data class Song(
        val name: String,
        val id: Long,
        val ar: List<Artist>,
        val al: Album
    )

    data class Artist(
        val id: Long,
        val name: String
    )

    data class Album(
        @SerializedName("picUrl")
        val cover: String,
    )
}