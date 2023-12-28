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

import cn.rtast.rmusic.utils.LoginManager
import cn.rtast.rmusic.utils.LyricParser
import cn.rtast.rmusic.utils.MusicPlayer
import cn.rtast.rmusic.utils.NetEaseMusic
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.server.command.CommandManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread


class RMusic : ModInitializer {

    companion object {
        val logger: Logger = LoggerFactory.getLogger("RMusic-main")
        val player = MusicPlayer()
        val loginManager = LoginManager()
        
        val minecraftClient: MinecraftClient = MinecraftClient.getInstance()
    }

    override fun onInitialize() {
        logger.info("RMusic($VERSION) 已加载!")

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(CommandManager.literal("play").executes {
                thread {
                    val parser = LyricParser()
                    val net = NetEaseMusic()
                    val id = 1469483755L
                    val lyric = net.getLyric(id)
                    val song = net.getSongUrl(id)
                    println(lyric)
                    val player = MusicPlayer()
                    val parsed = parser.parse(lyric)
                    player.playMusic(song, parsed)
                    println(parsed)
                }
                1
            })
        }

    }
}
