/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.kugou

import com.google.gson.annotations.SerializedName

data class KuGouLyricSearch(
    val candidates: Candidates
) {
    data class Candidates(
        val id: String,
        @SerializedName("accesskey")
        val accessKey: String
    )
}