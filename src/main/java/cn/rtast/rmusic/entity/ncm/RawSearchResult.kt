/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.entity.ncm

data class RawSearchResult(
    val result: Result
) {
    data class Result(
        val songs: List<Song>
    )

    data class Song(
        val id: Long,
        val name: String,
        val artists: List<Artist>,
        val album: Album
    )

    data class Artist(
        val id: Long,
        val name: String,
    )

    data class Album(
        val id: Long,
        val name: String,
    )
}