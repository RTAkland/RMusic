/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.util.config

import cn.rtast.rmusic.defaultConfig
import cn.rtast.rmusic.entity.Config
import net.mamoe.yamlkt.Yaml
import net.mamoe.yamlkt.Yaml.Default
import java.io.File

class ConfigManager {

    private val dir = File("./config/rmusic").apply { mkdirs() }
    private val file = File(dir, "config.yml")
    var config: Config? = null

    init {
        if (!file.exists()) {
            file.createNewFile()
            this.default()
        }
        config = this.read()
    }

    private fun default() {
        this.write(defaultConfig)
    }

    private fun write(data: Config) {
        val serializedData = Yaml.encodeToString(Config.serializer(), data)
        this.file.writeText(serializedData)
    }

    private fun read(): Config {
        return Default.decodeFromString(Config.serializer(), file.readText())
    }

    fun reload() {
        this.config = this.read()
    }
}