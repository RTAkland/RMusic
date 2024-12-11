/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/11
 */


package cn.rtast.rmusic.util.music

import tk.fanamusic.youtubemusicapi.YoutubeMusicAPI
import tk.fanamusic.youtubemusicapi.request.requests.VideoInfoRequest

object Youtube {

}

fun main() {
    val api = YoutubeMusicAPI.Builder()
        .setBaseURL("https://proxy.rtast.cn/https/music.youtube.com/youtubei/v1/")
        .build()
    val response = api.request(VideoInfoRequest("5svT9dfXPYw"))
    println(response.detail)
    println(response.response)
    println(response.streamingData)
}