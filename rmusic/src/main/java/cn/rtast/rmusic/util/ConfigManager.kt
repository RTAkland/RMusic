/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.entity.Config
import cn.rtast.rmusic.util.str.fromJson
import cn.rtast.rmusic.util.str.toJson
import java.io.File

class ConfigManager {

    private val dir = File("./config/rmusic").apply { mkdirs() }
    private val file = File(dir, "config.json")
    var config: Config? = null

    init {
        if (!file.exists()) {
            file.createNewFile()
            this.default()
        }
        config = this.read()
    }

    fun default() {
        file.writeText(Config("https://ncm.rtast.cn", true, "http://127.0.0.1:7979").toJson())
    }

    fun write(data: Config) {
        file.writeText(data.toJson())
    }

    fun read(): Config {
        return file.readText().fromJson<Config>()
    }

    fun reload() {
        this.config = this.read()
    }
}