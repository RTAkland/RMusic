/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.qq

data class GetSongLyric(
    val response: Response
) {
    data class Response(
        val lyric: String
    )
}