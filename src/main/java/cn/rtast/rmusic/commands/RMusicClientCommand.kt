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


package cn.rtast.rmusic.commands

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.utils.SessionManager
import cn.rtast.rmusic.utils.music.MusicPlayer
import cn.rtast.rmusic.utils.music.NetEaseMusic
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.net.URL
import kotlin.concurrent.thread

class RMusicClientCommand : IRMusicCommand {

    private val netease = NetEaseMusic()

    override fun executePlay(ctx: CommandContext<ServerCommandSource>, songId: String) {
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        }

        if (RMusicClient.player!!.isPausedOrPlaying || RMusicClient.player!!.mute) {
            this.executeStop(ctx)
        }
        thread {
            val url = this.netease.getSongUrl(songId)
            RMusicClient.player!!.play(URL(url))
            ctx.source.sendMessage(Text.translatable("rmusic.tip.playing", songId))
        }
    }

    override fun executeStop(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        }

        if (RMusicClient.player!!.isPausedOrPlaying) {
            RMusicClient.player!!.stop()
        }
    }

    override fun executeMute(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        }

        if (!RMusicClient.player!!.isStopped) {
            if (!RMusicClient.player!!.mute) {
                ctx.source.sendMessage(Text.translatable("rmusic.tip.mute.success"))
            } else {
                ctx.source.sendMessage(Text.translatable("rmusic.tip.mute.failed"))
            }
            RMusicClient.player!!.mute = !RMusicClient.player!!.mute
        }
    }

    override fun executePause(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        }

        if (RMusicClient.player!!.isPlaying) {
            RMusicClient.player!!.pause()
            ctx.source.sendMessage(Text.translatable("rmusic.tip.pause.success"))
        } else {
            ctx.source.sendMessage(Text.translatable("rmusic.tip.pause.failed"))
        }
    }

    override fun executeResume(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        }

        if (RMusicClient.player!!.isPausedOrPlaying) {
            ctx.source.sendMessage(Text.translatable("rmusic.tip.resume.success"))
        } else {
            ctx.source.sendMessage(Text.translatable("rmusic.tip.resume.failed"))
        }
    }

    override fun executeEmailLogin(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        ctx.source.sendMessage(Text.translatable("rmusic.tip.logging"))
        thread {
            val resp = this.netease.loginEmailPwd(email, password)
            if (resp == null) {
                ctx.source.sendMessage(Text.translatable("rmusic.tip.login.failed"))
            } else {
                SessionManager.setSessionCookie(resp)
                ctx.source.sendMessage(Text.translatable("rmusic.tip.login.success"))
            }
        }
    }

    override fun executePhoneLogin(ctx: CommandContext<ServerCommandSource>, cellphone: String, password: String) {
        ctx.source.sendMessage(Text.translatable("rmusic.tip.logging"))
        thread {
            val resp = this.netease.loginCellphonePwd(cellphone, password)
            if (resp == null) {
                ctx.source.sendMessage(Text.translatable("rmusic.tip.login.failed"))
            } else {
                SessionManager.setSessionCookie(resp)
                ctx.source.sendMessage(Text.translatable("rmusic.tip.login.success"))
            }
        }
    }

    override fun executeLogout(ctx: CommandContext<ServerCommandSource>) {
        if (SessionManager.loginState) {
            SessionManager.removeSessionCookie()
            ctx.source.sendMessage(Text.translatable("rmusic.tip.logout.success"))
        } else {
            ctx.source.sendMessage(Text.translatable("rmusic.tip.logout.failed"))
        }
    }

    override fun executeSearch(ctx: CommandContext<ServerCommandSource>, keyword: String, limit: Int) {
        ctx.source.sendMessage(Text.translatable("rmusic.tip.search", keyword))
        thread {
            val resp = this.netease.search(keyword, limit)
            resp.result.songs.forEach { song ->
                val artists = StringBuilder()
                song.artists.forEach { art ->
                    artists.append(art.name + "|")
                }
                ctx.source.sendMessage(
                    Text.translatable(
                        "rmusic.tip.search.success.result",
                        Text.literal(song.name).styled { it.withColor(Formatting.YELLOW) },
                        Text.literal(artists.toString()).styled { it.withColor(Formatting.YELLOW) },
                        Text.literal("▶").styled {
                            it.withColor(Formatting.GREEN)
                                .withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm play ${song.id}"))
                                .withHoverEvent(
                                    HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        Text.literal(song.id.toString())
                                    )
                                )
                        }
                    )
                )
            }
        }
    }
}