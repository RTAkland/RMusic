/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.entity.ncm

data class GetSongUrl(
    val data: List<Data>
) {
    data class Data(
        val url: String,
    )
}