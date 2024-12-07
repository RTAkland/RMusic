/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.entity.kugou

data class KuGouLogin(
    val data: Data,
    val status: Int,
) {
    data class Data(
        val nickname: String,
        val token: String,
    )
}