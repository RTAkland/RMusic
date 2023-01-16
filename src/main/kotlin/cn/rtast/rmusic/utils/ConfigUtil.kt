/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/12 20:48
 */

package cn.rtast.rmusic.utils

import java.io.File

class ConfigUtil {
    private val path = "./config/rmusic"
    private val configFile = File("$path/config.txt")
    private val dir = File(path)

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
        if (!configFile.exists()) {
            configFile.createNewFile()
            configFile.writeText("https://music.api.rtast.cn:444")
        }
    }

    fun get163URL(): String {
        return configFile.readText()
    }

    fun set163URL(url: String) {
        configFile.delete()
        configFile.createNewFile()
        configFile.writeText(url)
    }
}