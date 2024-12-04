/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity

import cn.rtast.rmusic.enums.LyricPosition

data class Config(
    val api: String,
    val autoPause: Boolean,
    val qqMusicApi: String,
    val position: LyricPosition,
)