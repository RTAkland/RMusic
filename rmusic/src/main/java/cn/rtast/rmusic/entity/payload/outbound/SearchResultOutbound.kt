/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.entity.payload.outbound

data class SearchResultOutbound(
    val result: List<Result>
) {
    data class Result(
        val id: String,
        val songName: String,
        val artistName: String,
    )
}