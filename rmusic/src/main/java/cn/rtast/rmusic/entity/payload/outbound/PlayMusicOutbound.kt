/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.entity.payload.outbound

data class PlayMusicOutbound(
    val url: String,
    val id: String,
    val songName: String,
    val artistName: String,
    val cover: String,  // url
    val lyric: Map<Int, String>,
)