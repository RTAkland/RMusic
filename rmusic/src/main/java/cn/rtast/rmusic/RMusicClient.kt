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

import cn.rtast.rmusic.command.RMusicCommand
import cn.rtast.rmusic.network.registerClientReceiver
import cn.rtast.rmusic.util.ConfigManager
import cn.rtast.rmusic.util.CookieManager
import cn.rtast.rmusic.util.MusicPlayer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.text.Text
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class RMusicClient : ClientModInitializer {
    companion object {
        val logger: Logger = LoggerFactory.getLogger("RMusic-main")
        val player = MusicPlayer()
        val loginManager = CookieManager()
        val configManager = ConfigManager()
    }

    override fun onInitializeClient() {
        registerClientReceiver()
        ClientCommandRegistrationCallback.EVENT.register(RMusicCommand())
        logger.info(Text.translatable("rmusic.loaded").string)
    }
}
