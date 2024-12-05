/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.entity.payload.outbound

data class QRCodeLoginOutbound(
    val key: String,
    val qrcodeBase64: String,
)