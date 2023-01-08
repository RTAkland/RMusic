package cn.rtast.rmusic.data.netease.search

data class Result(
    val hasMore: Boolean,
    val songCount: Int,
    val songs: List<Song>
)