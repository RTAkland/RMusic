/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.entity.ncm

import com.google.gson.annotations.SerializedName

data class CreateQRCodeImage(
    val data: Data
) {
    data class Data(
        @SerializedName("qrimg")
        val base64Image: String
    )
}