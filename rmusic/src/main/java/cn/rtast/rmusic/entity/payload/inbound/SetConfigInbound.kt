/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.entity.payload.inbound

import cn.rtast.rmusic.entity.config.ServerConfig

data class SetConfigInbound(
    val config: ServerConfig,
)