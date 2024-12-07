/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.kugou

import com.google.gson.annotations.SerializedName

data class KuGouSongInfo(
    val data: List<Data>
) {
    data class Data(
        @SerializedName("timelength")
        val timeLength: String,
        val hash: String,
        @SerializedName("audio_name")
        val audioName: String,
    )
}