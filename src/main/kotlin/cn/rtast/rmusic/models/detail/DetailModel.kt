package cn.rtast.rmusic.models.detail

data class DetailModel(
    val code: Int,
    val privileges: List<Privilege>,
    val songs: List<Song>
)