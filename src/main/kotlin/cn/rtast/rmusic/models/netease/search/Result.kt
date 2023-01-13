package cn.rtast.rmusic.models.netease.search

data class Result(
    val hasMore: Boolean,
    val songCount: Int,
    val songs: List<Song>
)