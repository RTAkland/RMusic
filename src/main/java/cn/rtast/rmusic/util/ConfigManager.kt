/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.entity.Config
import java.io.File

class ConfigManager {

    private val dir = File("./config/rmusic").apply { mkdirs() }
    private val file = File(dir, "config.json")

    init {
        if (!file.exists()) {
            file.createNewFile()
            this.default()
        }
    }

    fun default() {
        file.writeText(Config("https://ncm.rtast.cn").toJson())
    }

    fun write(data: Config) {
        file.writeText(data.toJson())
    }

    fun read(): Config {
        return file.readText().fromJson<Config>()
    }
}