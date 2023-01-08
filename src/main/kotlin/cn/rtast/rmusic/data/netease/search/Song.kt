package cn.rtast.rmusic.data.netease.search

data class Song(
    val album: Album,
    val alias: List<Any>,
    val artists: List<Artists>,
    val copyrightId: Int,
    val duration: Int,
    val fee: Int,
    val ftype: Int,
    val id: Int,
    val mark: Long,
    val mvid: Int,
    val name: String,
    val rUrl: Any,
    val rtype: Int,
    val status: Int
)