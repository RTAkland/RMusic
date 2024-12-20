/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.payload.outbound

import cn.rtast.rmusic.enums.MusicPlatform

data class ShareMusicOutbound(
    val name: String,
    val id: String,
    val artists: String,
    val songUrl: String,
    val fromWho: String,
    val platform: MusicPlatform
)