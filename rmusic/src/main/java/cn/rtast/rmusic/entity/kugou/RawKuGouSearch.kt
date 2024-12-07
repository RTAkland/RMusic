/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.kugou

import com.google.gson.annotations.SerializedName

data class RawKuGouSearch(
    val data: Data
) {
    data class Data(
        val lists: List<Result>
    )

    data class Result(
        @SerializedName("SongName")
        val songName: String,
        @SerializedName("SingerName")
        val singerName: String,
        @SerializedName("HQDuration")
        val duration: Int,
        @SerializedName("Audioid")
        val audioId: String,
        @SerializedName("SQFileHash")
        val fileHash: String,
    )
}