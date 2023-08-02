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

package cn.rtast.rmusic.command

import cn.rtast.rmusic.api.Netease
import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.utils.StreamMusicPlayer
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg
import com.mojang.brigadier.arguments.DoubleArgumentType.getDouble
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import kotlin.concurrent.thread

open class RMusicCommand {
    private fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val root = dispatcher.register(
            literal("rmusic")
                .then(literal("play").then(argument("songId", string()).executes { this.play(getString(it, "songId"), it);1 }))
                .then(literal("mute").executes { this.mute(it);1 })
                .then(literal("unmute").executes { this.unmute(it);1 })
                .then(literal("resume").executes { this.resume(it);1 })
                .then(literal("stop").executes { this.stop(it);1 })
                .then(literal("pause").executes { this.pause(it);1 })
                .then(literal("volume").then(argument("numerical", doubleArg(0.0, 20.0)).executes { this.volume(getDouble(it , "numerical"), it);1 }))
                .then(literal("login").requires { it.hasPermissionLevel(2) }).executes { this.login(it);1 }
        )
    }

    open fun play(songId: String, context: CommandContext<ServerCommandSource>) {
        if (RMusicClient.musicPlayer == null) {
            RMusicClient.musicPlayer = StreamMusicPlayer()
        }
        thread {
            val songUrl = Netease().getSongUrl(songId.toLong())
            RMusicClient.musicPlayer!!.playMusic(songUrl)
        }
    }

    open fun stop(context: CommandContext<ServerCommandSource>) {
        if (RMusicClient.musicPlayer != null) {
            RMusicClient.musicPlayer!!.stop()
        } else {
            //
        }
    }

    open fun pause(context: CommandContext<ServerCommandSource>) {

    }

    open fun resume(context: CommandContext<ServerCommandSource>) {

    }

    open fun mute(context: CommandContext<ServerCommandSource>) {

    }

    open fun unmute(context: CommandContext<ServerCommandSource>) {

    }

    open fun search(keyword: String, context: CommandContext<ServerCommandSource>) {

    }

    open fun login(context: CommandContext<ServerCommandSource>) {

    }

    open fun volume(volume: Double, context: CommandContext<ServerCommandSource>) {

    }
}