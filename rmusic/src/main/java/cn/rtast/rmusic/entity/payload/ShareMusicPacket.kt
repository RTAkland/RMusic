/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.payload

data class ShareMusicPacket(
    val name: String,
    val id: Long,
    val artists: String,
    val songUrl: String,
    val fromWho: String,
)