package cn.rtast.rmusic.entities.search

data class Result(
    val songCount: Int,
    val songs: List<Song>
)