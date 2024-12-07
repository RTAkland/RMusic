/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.entity

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val neteaseMusicAPI: String,
    val qqMusicAPI: String,
)