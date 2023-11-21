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
package cn.rtast.rmusic

import cn.rtast.rmusic.enums.logger.Level
import cn.rtast.rmusic.utils.MusicPlayer
import cn.rtast.rmusic.utils.logger.ConsoleLogger
import com.google.gson.Gson
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import java.net.URL

class RMusic : ModInitializer {

    companion object {
        val logger = ConsoleLogger()
        val gson = Gson()
        var API = "https://rmusic.dgtmc.top"
        val RNetworkingChannel = Identifier("rmusic", "channel")
    }

    override fun onInitialize() {

    }
}

fun main() {
    RMusic.logger.setLogLevel(Level.DEBUG)
    val url = "https://static.rtakland.icu/files/res/music.mp3"
    MusicPlayer().play(URL(url))
}