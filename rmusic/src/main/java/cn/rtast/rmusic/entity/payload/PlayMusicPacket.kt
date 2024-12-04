/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.entity.payload

data class PlayMusicPacket(
    val songUrl: String,
    val lyric: Map<Int, String>,
    val songName: String,
    val cover: String,
    val id: String,
    val artists: String,
)