/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/12 20:48
 */

package cn.rtast.rmusic.utils

import cn.rtast.rmusic.models.config.Config
import cn.rtast.rmusic.models.config.ConfigModel
import com.google.gson.Gson
import java.io.File

class ConfigUtil {
    private val gson = Gson()
    private val configFile = File("./config/rmusic/config.json")
    private val dir = File("./config/rmusic")

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
        if (!configFile.exists()) {
            configFile.createNewFile()
            val config = ConfigModel(listOf(Config("163", "https://api.163.rtast.cn", true)))
            configFile.writeText(gson.toJson(config).toString())
        }
    }

    fun getURL(platform: String): String {
        val allConfig = gson.fromJson(configFile.readText(), ConfigModel::class.java)
        var url = ""
        for (i in allConfig.configs) {
            if (i.platform == platform) {
                url = i.url
                break
            }
        }
        return url
    }
}