/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.kugou

import com.google.gson.annotations.SerializedName

data class GetKuGouQRCode(
    val data: Data
) {
    data class Data(
        val qrcode: String,
        @SerializedName("qrcode_img")
        val qrcodeImg: String,
    )
}