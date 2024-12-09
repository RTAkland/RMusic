/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.enums

enum class MusicPlatform(val platform: String) {
    Netease("netease"), QQ("qq"), KuGou("kugou");

    companion object {
        fun fromPlatform(platform: String): MusicPlatform {
            return when (platform) {
                Netease.platform -> Netease
                QQ.platform -> QQ
                KuGou.platform -> KuGou
                else -> Netease
            }
        }
    }
}