/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/12 20:48
 */

package cn.rtast.rmusic.utils

import cn.rtast.rmusic.models.ConfigModel
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
            val config = ConfigModel("https://api.163.rtast.cn")
            configFile.writeText(gson.toJson(config).toString())
        }
    }

    fun get163URL(): String {
        return gson.fromJson(configFile.readText(), ConfigModel::class.java).netease
    }
}