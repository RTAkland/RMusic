/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/9
 */


package cn.rtast.rmusic.entity

import cn.rtast.rmusic.enums.MusicPlatform

data class PlaylistItem(
    val id: String,
    val name: String,
    val artist: String,
    val platform: MusicPlatform
)