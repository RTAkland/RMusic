/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.entity.qq

data class GetAlbumInfo(
    val response: Response
) {
    data class Response(
        val data: Data
    )

    data class Data(
        val singername: String,
    )
}