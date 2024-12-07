/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.entity.ncm

data class SongDetail(
    val name: String,
    val id: String,
    val cover: String,
    val artists: String,
    val duration: Long,
)