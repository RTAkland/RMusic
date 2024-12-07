/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.entity.payload.outbound

import cn.rtast.rmusic.enums.MusicPlatform

data class QRCodeLoginOutbound(
    val key: String,
    val qrcodeBase64: String,
    val platform: MusicPlatform
)