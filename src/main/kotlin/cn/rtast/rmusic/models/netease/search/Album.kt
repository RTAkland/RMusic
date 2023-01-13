package cn.rtast.rmusic.models.netease.search

data class Album(
    val artist: ArtistX,
    val copyrightId: Int,
    val id: Int,
    val mark: Int,
    val name: String,
    val picId: Long,
    val publishTime: Long,
    val size: Int,
    val status: Int
)