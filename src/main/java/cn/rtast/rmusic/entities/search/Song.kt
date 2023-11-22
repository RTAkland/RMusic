package cn.rtast.rmusic.entities.search

data class Song(
    val artists: List<Artist>,
    val duration: Int,
    val id: Long,
    val name: String,
)