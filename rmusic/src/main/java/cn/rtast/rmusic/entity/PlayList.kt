/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/8
 */


package cn.rtast.rmusic.entity

import cn.rtast.rmusic.enums.MusicPlatform

data class PlayList(
    val id: String,
    val platform: MusicPlatform
)