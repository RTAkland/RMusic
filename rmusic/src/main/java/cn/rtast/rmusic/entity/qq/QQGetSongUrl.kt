/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.qq

data class QQGetSongUrl(
    val data: Data
) {
    data class Data(
        val playUrl: Map<String, PlayUrl>
    )

    data class PlayUrl(
        val url: String
    )
}