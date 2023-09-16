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

package cn.rtast.rmusic.client.commands

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.commands.BaseCommand
import cn.rtast.rmusic.utils.CloudMusic
import cn.rtast.rmusic.utils.Message
import com.goxr3plus.streamplayer.enums.Status
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.concurrent.thread

class RMusicClientCommand : BaseCommand {

    override fun executePlay(source: CommandContext<ServerCommandSource>, songId: String) {
        val message = Message(source)
        thread {
            RMusicClient.player.play(songId.toLong())
        }
        message.sendMessage(
            Text.translatable("player.play.waiting", Text.literal(songId).styled {
                it.withColor(Formatting.GOLD)
                it.withItalic(true)
            }.styled {
                it.withColor(Formatting.GREEN)
            })
        )
    }

    override fun executeStop(source: CommandContext<ServerCommandSource>) {
        val message = Message(source)
        if (RMusicClient.player.status == Status.STOPPED) {
            message.sendMessage(Text.translatable("player.notplaying").styled { it.withColor(Formatting.RED) })
        } else {
            RMusicClient.player.stop()
        }

    }

    override fun executePause(source: CommandContext<ServerCommandSource>) {
        val message = Message(source)
        when (RMusicClient.player.status) {
            Status.PLAYING -> {
                RMusicClient.player.pause()
                message.sendMessage(Text.translatable("player.pause.off").styled { it.withColor(Formatting.GREEN) })
            }

            Status.STOPPED -> {
                message.sendMessage(Text.translatable("player.notplaying").styled { it.withColor(Formatting.RED) })
            }

            Status.PAUSED -> {
                message.sendMessage(Text.translatable("player.pause.already").styled { it.withColor(Formatting.RED) })
            }

            else -> {
                println("Unknown Status")
            }
        }

    }

    override fun executeResume(source: CommandContext<ServerCommandSource>) {
        val message = Message(source)
        when (RMusicClient.player.status) {
            Status.PAUSED -> {
                RMusicClient.player.resume()
                message.sendMessage(Text.translatable("player.resume").styled { it.withColor(Formatting.GREEN) })
            }

            else -> {
                println("Unknown Status")
            }
        }
    }

    override fun executeVolume(source: CommandContext<ServerCommandSource>, value: Double) {
        val message = Message(source)
        RMusicClient.player.setGain(value * 0.004)

    }

    override fun executeLoginWithEmail(source: CommandContext<ServerCommandSource>, email: String, passwd: String) {
        val message = Message(source)

    }

    override fun executeLoginWithQRCode(source: CommandContext<ServerCommandSource>) {
        val message = Message(source)
        thread {
            val key = CloudMusic().getQRCodeKey()
            val qrcodeUrl = CloudMusic().getQRCodeImage(key).qrcodeUrl
            message.sendMessage(Text.translatable(""))
        }

    }

    override fun executeLoginWithCellphone(
        source: CommandContext<ServerCommandSource>, cellphone: Long, passwd: String?
    ) {
        val message = Message(source)

    }

    override fun executeLogout(source: CommandContext<ServerCommandSource>) {
        val message = Message(source)

    }

    override fun executeVerifyQRCode(source: CommandContext<ServerCommandSource>, key: String) {
        val message = Message(source)

    }

    override fun executeVerifyCaptcha(source: CommandContext<ServerCommandSource>, captcha: String) {
        val message = Message(source)

    }

    override fun executeSetAPIHost(source: CommandContext<ServerCommandSource>, newHost: String) {
        val message = Message(source)

    }
}