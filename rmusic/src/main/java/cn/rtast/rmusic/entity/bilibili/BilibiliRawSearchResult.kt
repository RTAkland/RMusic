/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/11
 */


package cn.rtast.rmusic.entity.bilibili

import com.google.gson.annotations.SerializedName

data class BilibiliRawSearchResult(
    val data: Data
) {
    data class Data(
        val result: List<Result>
    )

    data class Result(
        @SerializedName("result_type")
        val resultType: String,
        val data: List<Any>
    )
}