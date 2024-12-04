/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.entity.payload

data class QRCodeLoginPacket(
    val key: String,
    // byteArray -> base64
    val qrcode: String,
)

data class PhoneAndPasswordPacket(
    val phone: String,
    val password: String
)

data class PhoneAndCaptchaPacket(
    val phone: String
)

data class EMailAndPasswordPacket(
    val email: String,
    val password: String
)