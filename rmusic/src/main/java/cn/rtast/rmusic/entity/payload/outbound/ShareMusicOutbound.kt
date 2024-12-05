/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.payload.outbound

data class ShareMusicOutbound(
    val name: String,
    val id: String,
    val artists: String,
    val songUrl: String,
    val fromWho: String,
)