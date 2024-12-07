/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.entity.payload.inbound

import cn.rtast.rmusic.enums.MusicPlatform

data class SearchPayloadInbound(
    val keyword: String,
    val searchPlatform: MusicPlatform,
)