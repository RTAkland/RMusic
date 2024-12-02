/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.qrcodeId
import cn.rtast.rmusic.util.*
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.net.URI

class RMusicCommand {

    private val scope = CoroutineScope(Dispatchers.IO)

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommandManager.literal("rm")
                .then(
                    ClientCommandManager.literal("search")
                        .then(
                            argument("keyword", StringArgumentType.string()).executes { context ->
                                val keyword = context.getArgument("keyword", String::class.java)
                                context.source.sendFeedback(Text.literal("正在搜索: $keyword"))
                                scope.launch {
                                    val result = NCMusic.search(keyword)
                                    val text = Text.literal("搜索到如下结果: \n")
                                    result.take(5).forEach { r ->
                                        val songText = Text.of("${r.name} - ${r.artists}")
                                        val clickableText = Text.literal(" [▶]").styled {
                                            it.withColor(Formatting.YELLOW)
                                                .withClickEvent(
                                                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm play ${r.id}")
                                                )
                                                .withHoverEvent(
                                                    HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT,
                                                        Text.literal("点击播放")
                                                            .styled { it.withColor(Formatting.GREEN) })
                                                )
                                        }
                                        text.append(songText).append(clickableText).append(Text.literal("\n"))
                                    }
                                    context.source.sendFeedback(text)
                                }

                                0
                            }
                        )
                ).then(
                    ClientCommandManager.literal("play")
                        .then(
                            argument("songId", LongArgumentType.longArg(0)).executes { context ->
                                val songId = context.getArgument("songId", Long::class.java)
                                scope.launch {
                                    try {
                                        context.source.sendFeedback(Text.literal("正在获取歌曲信息..."))
                                        val songUrl = NCMusic.getSongUrl(songId)
                                        val lyric = NCMusic.getLyric(songId)
                                        RMusic.player.playMusic(songUrl, lyric)
                                        val songDetail = NCMusic.getSongDetail(songId)
                                        renderSongDetail(songDetail)
                                        val coverBytes =
                                            URI(songDetail.cover + "?param=128y128").toURL()
                                                .readBytes().toPNG()
                                        renderCover(coverBytes)
                                        context.source.sendFeedback(Text.literal("正在播放: ${songDetail.name} - 《${songDetail.artists}》"))
                                    } catch (_: NullPointerException) {
                                        context.source.sendFeedback(Text.literal("播放音乐失败(可能是没有登陆播放了付费歌曲)"))
                                    }
                                }
                                0
                            }
                        )
                ).then(
                    ClientCommandManager.literal("pause")
                        .executes { context ->
                            RMusic.player.pause()
                            context.source.sendFeedback(Text.literal("已暂停"))
                            0
                        }
                ).then(
                    ClientCommandManager.literal("resume")
                        .executes { context ->
                            RMusic.player.resume()
                            context.source.sendFeedback(Text.literal("已恢复播放"))
                            0
                        }
                ).then(
                    ClientCommandManager.literal("stop")
                        .executes { context ->
                            RMusic.player.stop()
                            context.source.sendFeedback(Text.literal("已停止播放"))
                            0
                        }
                ).then(
                    ClientCommandManager.literal("login")
                        .executes { context ->
                            context.source.sendFeedback(Text.literal("正在获取二维码..."))
                            scope.launch {
                                loadQRCode = false
                                val (key, qrcode) = NCMusic.loginByQRCode()
                                renderQRCode(qrcode)
                                context.source.sendFeedback(
                                    Text.literal("扫码并登录完成后点击").append(Text.literal("[这里]").styled {
                                        it.withColor(Formatting.GREEN).withHoverEvent(
                                            HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                Text.literal("点击这里确认登录状态")
                                                    .styled { it.withColor(Formatting.GREEN) }
                                            )
                                        ).withClickEvent(
                                            ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm login confirm $key")
                                        )
                                    })
                                )
                            }
                            0
                        }.then(
                            ClientCommandManager.literal("state")
                                .executes { context ->
                                    if (RMusic.loginManager.getCookie() != null) {
                                        context.source.sendFeedback(Text.literal("你已登录"))
                                    } else {
                                        context.source.sendFeedback(Text.literal("你还未登录"))
                                    }
                                    0
                                }
                        ).then(
                            ClientCommandManager.literal("logout")
                                .executes { context ->
                                    RMusic.loginManager.logout()
                                    context.source.sendFeedback(Text.literal("已退出登录"))
                                    0
                                }
                        ).then(
                            ClientCommandManager.literal("confirm")
                                .then(
                                    argument("qrcodeKey", StringArgumentType.string()).executes { context ->
                                        context.source.sendFeedback(Text.literal("正在检查登录状态中"))
                                        scope.launch {
                                            loadQRCode = false
                                            destroyTexture(qrcodeId)
                                            val qrCodeKey = context.getArgument("qrcodeKey", String::class.java)
                                            val cookie = NCMusic.checkQRCodeStatus(qrCodeKey)
                                            if (cookie != null) {
                                                RMusic.loginManager.login(cookie)
                                                val accountInfo = NCMusic.getUserAccount()
                                                context.source.sendFeedback(Text.literal("登陆成功, 用户名: $accountInfo"))
                                            } else {
                                                context.source.sendFeedback(Text.literal("验证失败, 请检查是否扫码并成功登录"))
                                            }
                                        }
                                        0
                                    }
                                )
                        )
                )
        )
    }
}