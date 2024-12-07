/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.kugou

import com.google.gson.annotations.SerializedName

data class GetKuGouImage(
    val data: List<Data>
) {
    data class Data(
        val author: List<Author>,
        val album: List<Author>,
    )

    data class Author(
        @SerializedName("sizable_avatar")
        val sizableAvatar: String,
    )
}