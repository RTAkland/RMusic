/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.util.config

import cn.rtast.rmusic.entity.config.ServerConfig
import cn.rtast.rmusic.util.str.fromJson
import cn.rtast.rmusic.util.str.toJson
import java.io.File

class ServerConfigManager {

    private val dir = File("./config/rmusic")
    private val file = File(dir, "config.json")
    var config: ServerConfig? = null

    init {
        if (!file.exists()) {
            file.createNewFile()
            this.default()
        }
        config = this.read()
    }

    private fun default() {
        file.writeText(
            ServerConfig("https://ncm.rtast.cn", "http://127.0.0.1:6868").toJson()
        )
    }

    fun write(data: ServerConfig) {
        file.writeText(data.toJson())
    }

    fun read(): ServerConfig {
        return file.readText().fromJson<ServerConfig>()
    }

    fun reload() {
        this.config = this.read()
    }
}