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
import cn.rtast.rmusic.utils.toBitmap
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier

class RMusic : ModInitializer {

    companion object {
        const val DEFAULT_CONF_PATH = "./config/rmusic"
        var API_HOST = "https://zm.armoe.cn"
        val CHANNEL_ID = Identifier("rmusic", "channel")
    }

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register(RMusicCommand())
    }
}

//fun main() {
//    val a = "abc".toBitmap()
//    for (i in a) {
//        for (l in i) {
//            if (l == 1) {
//                print("1")
//            } else {
//                print("0")
//            }
//        }
//        println()
//    }
//}