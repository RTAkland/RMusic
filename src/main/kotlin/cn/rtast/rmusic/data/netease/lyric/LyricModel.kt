package cn.rtast.rmusic.data.netease.lyric

data class LyricModel(
    val code: Int,
    val klyric: Klyric,
    val lrc: Lrc,
    val qfy: Boolean,
    val romalrc: Romalrc,
    val sfy: Boolean,
    val sgc: Boolean,
    val tlyric: Tlyric
)