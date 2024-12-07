/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.qq

data class GetQQCoverImage(
    val response: Response
) {
    data class Response(
        val data: Data
    )

    data class Data(
        val imageUrl: String
    )
}