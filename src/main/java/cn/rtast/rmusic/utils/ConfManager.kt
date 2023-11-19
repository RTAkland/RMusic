/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package cn.rtast.rmusic.utils

import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfManager(private val configFile: File) {
    private val yaml = Yaml()
    private var configData: MutableMap<String, Any> = mutableMapOf()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        if (!configFile.exists()) {
            configFile.createNewFile()
            saveConfig()
        } else {
            val content = configFile.readText()
            configData = yaml.load(content) ?: mutableMapOf()
        }
    }

    private fun saveConfig() {
        val content = yaml.dump(configData)
        configFile.writeText(content)
    }

    fun setValue(key: String, value: Any) {
        configData[key] = value
        saveConfig()
    }

    fun getValue(key: String): Any? {
        return configData[key]
    }
}
