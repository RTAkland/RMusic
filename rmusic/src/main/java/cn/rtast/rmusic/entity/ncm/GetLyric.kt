/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.entity.ncm

data class GetLyric(
    val lrc: Lyric
) {
    data class Lyric(
        val lyric: String,
    )
}