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

import cn.rtast.rmusic.commands.RMusicCommand
import cn.rtast.rmusic.enums.Actions
import cn.rtast.rmusic.models.PayloadModel
import cn.rtast.rmusic.utils.ConsoleLogger
import cn.rtast.rmusic.utils.fromJson
import cn.rtast.rmusic.utils.toJson
import com.google.gson.Gson
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier

class RMusic : ModInitializer {

    companion object {
        val logger = ConsoleLogger()
        val gson = Gson()
        var API = "https://rmusic.dgtmc.top"
        val RNetworkChannel = Identifier("rmusic", "channel")
    }

    override fun onInitialize() {
        logger.info("RMusic($VERSION) 已加载!")
        CommandRegistrationCallback.EVENT.register(RMusicCommand())
    }
}
