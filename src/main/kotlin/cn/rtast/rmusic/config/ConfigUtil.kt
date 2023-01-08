/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/8 19:27
 */

package cn.rtast.rmusic.config

import cn.rtast.rmusic.data.config.ConfigModel
import cn.rtast.rmusic.data.config.Platform
import com.google.gson.Gson
import java.io.File

class ConfigUtil {

    private val gson = Gson()
    private val configFile = File("./config/rmusic/config.json")

    init {
        if (!configFile.exists()) {
            configFile.createNewFile()
            val config = ConfigModel(listOf(Platform("Netease", "https://api.163.rtast.cn")))
            val jsonString = gson.toJson(config).toString()
            configFile.writeText(jsonString)
        }
    }

    fun getApiAddr163(): String {
        val config = gson.fromJson(configFile.readText(), ConfigModel::class.java)
        return config.api[0].api
    }

    fun getApiAddrQQ() {
        TODO("no api available for QQ")
    }
}