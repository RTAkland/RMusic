/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.entity.payload

data class SearchResultPacket(
    val result: List<Result>
) {
    data class Result(
        val name: String,
        val id: String,
        val artists: String,
    )
}