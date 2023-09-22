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
import cn.rtast.rmusic.models.Login
import cn.rtast.rmusic.music.CloudMusic
import cn.rtast.rmusic.utils.Message
import cn.rtast.rmusic.utils.SessionManager
import com.goxr3plus.streamplayer.enums.Status
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.concurrent.thread

class RMusicClientCommand : BaseCommand {

    private fun loginStatus(message: Message, response: Login) {
        if (response.code != 200) {
            message.sendMessage(
                Text.translatable("session.netease.login.failure").styled { it.withColor(Formatting.RED) })
        } else {
            message.sendMessage(Text.translatable("session.netease.login.success",
                Text.literal(response.profile!!.nickname).styled { it.withColor(Formatting.AQUA) },
                Text.literal(response.profile.userId.toString()).styled {
                    it.withColor(Formatting.DARK_GREEN)
                }).styled {
                it.withColor(Formatting.GREEN)
                it.withItalic(true)
            })
        }
    }

    private fun loginStatusAnonymous(status: Boolean, message: Message) {
        if (status) {
            message.sendMessage(
                Text.translatable("session.netease.login.failure").styled { it.withColor(Formatting.RED) })
        } else {
            message.sendMessage(Text.translatable("session.netease.login.success", Text.literal("null").styled {
                it.withColor(Formatting.AQUA)
                it.withItalic(true)
            }, Text.literal("null").styled {
                it.withColor(Formatting.AQUA)
                it.withItalic(true)
            }).styled {
                it.withColor(Formatting.GREEN)
            })
        }
    }

    override fun executePlay(source: CommandContext<ServerCommandSource>, songId: String) {
        val message = Message(source)
        if (RMusicClient.player.status != Status.STOPPED) {
            thread {
                RMusicClient.player.play(songId.toLong())
            }
        } else {
            RMusicClient.player.stop()
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
        message.sendMessage(Text.translatable("player.setvolume", Text.literal((value * 0.004).toString()).styled {
            it.withColor(Formatting.AQUA)
        }).styled {
            it.withColor(Formatting.GREEN)
        })
    }

    override fun executeLoginWithEmail(source: CommandContext<ServerCommandSource>, email: String, passwd: String) {
        val message = Message(source)
        message.sendMessage(Text.translatable("session.netease.login.wait",
            Text.translatable("rmusic.mail.text").styled { it.withColor(Formatting.AQUA) }).styled {
            it.withColor(Formatting.GREEN)
        })
        thread {
            val resp = CloudMusic().loginWithEmail(email, passwd)
            this.loginStatus(message, resp)
        }
    }

    override fun executeLoginWithQRCode(source: CommandContext<ServerCommandSource>) {
        val message = Message(source)
        thread {
            val key = CloudMusic().getQRCodeKey()
            val qrcodeUrl = CloudMusic().getQRCodeImage(key).qrcodeUrl
            message.sendMessage(Text.translatable("rmusic.loginurl.text",
                Text.translatable("rmusic.here.text").styled { it.withColor(Formatting.AQUA) }).styled {
                it.withColor(Formatting.GREEN)
                it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, qrcodeUrl)).withHoverEvent(
                    HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        Text.literal(key).styled { key -> key.withColor(Formatting.AQUA) })
                )
            })
            message.sendMessage(Text.translatable("rmusic.login.qr.check",
                Text.translatable("rmusic.here.text").styled { it.withColor(Formatting.AQUA) }).styled {
                it.withColor(Formatting.GREEN)
                it.withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm verify $key"))
            })
        }

    }

    override fun executeLoginWithCellphone(
        source: CommandContext<ServerCommandSource>, cellphone: Long, passwd: String?
    ) {
        val message = Message(source)
        if (passwd == null) {
            thread {
                CloudMusic().sendCaptcha(cellphone)
            }
            message.sendMessage(Text.translatable(
                "rmusic.login.phone.captcha",
                Text.literal(cellphone.toString()).styled {
                    it.withColor(Formatting.AQUA)
                    it.withItalic(true)
                }).styled {
                it.withColor(Formatting.GREEN)
            })
        } else {
            thread {
                val loginResponse = CloudMusic().loginWithPhone(cellphone, passwd)
                this.loginStatus(message, loginResponse)
            }
        }
    }

    override fun executeLogout(source: CommandContext<ServerCommandSource>) {
        val message = Message(source)
        if (SessionManager().getStatus() == SessionManager.SessionStatus.LoggedIn) {
            message.sendMessage(Text.translatable("session.netease.logout.success").styled {
                it.withColor(Formatting.GREEN)
            })
        } else {
            message.sendMessage(Text.translatable("session.netease.logout.failure").styled {
                it.withColor(Formatting.RED)
            })
        }
    }

    override fun executeVerifyQRCode(source: CommandContext<ServerCommandSource>, key: String) {
        val message = Message(source)
        message.sendMessage(Text.translatable("rmusic.login.checking").styled {
            it.withColor(Formatting.GREEN)
        })
        thread {
            val response = CloudMusic().checkQRCode(key)
            if (response == null) {
                message.sendMessage(
                    Text.translatable("session.netease.login.failure").styled { it.withColor(Formatting.RED) })
            } else {
                SessionManager().setCookie(response)
                this.loginStatusAnonymous(true, message)
            }
        }
    }

    override fun executeVerifyCaptcha(source: CommandContext<ServerCommandSource>, cellphone: Long, captcha: String) {
        val message = Message(source)

        message.sendMessage(Text.translatable("session.netease.login.failure").styled { it.withColor(Formatting.RED) })

        thread {
            val verifyResponse = CloudMusic().verifyCaptcha(cellphone, captcha)
            this.loginStatusAnonymous(verifyResponse, message)
        }

    }

    override fun executeSetAPIHost(source: CommandContext<ServerCommandSource>, newHost: String) {
        val message = Message(source)
        SessionManager().setApiHost(newHost)
        message.sendMessage(Text.translatable("config.modify.new",
            Text.literal(newHost).styled { it.withColor(Formatting.AQUA)
            it.withItalic(true)}).styled {
            it.withColor(Formatting.GREEN)
        })
    }
}