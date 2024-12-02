/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.util.NCMusic
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class RMusicCommand : ClientCommandRegistrationCallback {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<FabricClientCommandSource>("rm")
                .then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("search")
                        .then(
                            argument("keyword", StringArgumentType.string()).executes { context ->
                                val keyword = context.getArgument("keyword", String::class.java)
                                scope.launch {
                                    val result = NCMusic.search(keyword)
                                    val text = Text.literal("搜索到如下结果: \n")
                                    result.take(5).forEach { r ->
                                        val songText = Text.of("${r.name} - ${r.artists}")
                                        val clickableText = Text.literal(" [▶]").styled {
                                            it.withColor(Formatting.YELLOW)
                                                .withClickEvent(
                                                    ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/rm play ${r.id}")
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
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("play")
                        .then(
                            argument("songId", LongArgumentType.longArg(0)).executes { context ->
                                val songId = context.getArgument("songId", Long::class.java)
                                scope.launch {
                                    try {

                                        val songUrl = NCMusic.getSongUrl(songId)
                                        val lyric = NCMusic.getLyric(songId)
                                        RMusic.player.playMusic(songUrl, lyric)
                                        context.source.sendFeedback(Text.literal("正在播放: $songId"))
                                    } catch (_: NullPointerException) {
                                        context.source.sendFeedback(Text.literal("播放音乐失败(可能是没有登陆播放了付费歌曲)"))
                                    }
                                }
                                0
                            }
                        )
                ).then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("pause")
                        .executes { context ->
                            RMusic.player.pause()
                            context.source.sendFeedback(Text.literal("已暂停"))
                            0
                        }
                ).then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("resume")
                        .executes { context ->
                            RMusic.player.resume()
                            context.source.sendFeedback(Text.literal("已恢复播放"))
                            0
                        }
                ).then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("stop")
                        .executes { context ->
                            RMusic.player.stop()
                            context.source.sendFeedback(Text.literal("已停止播放"))
                            0
                        }
                ).then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("login")
                        .executes { context ->
                            scope.launch {
                                val (key, url) = NCMusic.loginByQRCode()
                                context.source.sendFeedback(Text.literal("点面").append(Text.literal("[这里]").styled {
                                    it.withColor(Formatting.GREEN)
                                        .withHoverEvent(
                                            HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                Text.literal("点击这里打开浏览器扫码")
                                            )
                                        ).withClickEvent(
                                            ClickEvent(
                                                ClickEvent.Action.OPEN_URL,
                                                "https://api.rtast.cn/api/viewQrcode?key=${key}"
                                            )
                                        )
                                }).append(Text.literal(", 扫码登录完成后点击")).append(Text.literal("[这里]").styled {
                                    it.withColor(Formatting.GREEN)
                                        .withHoverEvent(
                                            HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                Text.literal("点击这里确认二维码状态")
                                            )
                                        )
                                        .withClickEvent(
                                            ClickEvent(
                                                ClickEvent.Action.RUN_COMMAND,
                                                "/rm login confirm $key"
                                            )
                                        )
                                }))
                            }
                            0
                        }.then(
                            LiteralArgumentBuilder.literal<FabricClientCommandSource>("state")
                                .executes { context ->
                                    if (RMusic.loginManager.getCookie() != null) {
                                        context.source.sendFeedback(Text.literal("你已登录"))
                                    } else {
                                        context.source.sendFeedback(Text.literal("你还未登录"))
                                    }
                                    0
                                }
                        ).then(
                            LiteralArgumentBuilder.literal<FabricClientCommandSource>("logout")
                                .executes { context ->
                                    RMusic.loginManager.logout()
                                    context.source.sendFeedback(Text.literal("已退出登录"))
                                    0
                                }
                        ).then(
                            LiteralArgumentBuilder.literal<FabricClientCommandSource>("confirm")
                                .then(
                                    argument("qrcodeKey", StringArgumentType.string()).executes { context ->
                                        scope.launch {
                                            val qrCodeKey = context.getArgument("qrcodeKey", String::class.java)
                                            val cookie = NCMusic.checkQRCodeStatus(qrCodeKey)
                                            if (cookie != null) {
                                                RMusic.loginManager.login(cookie)
                                                context.source.sendFeedback(Text.literal("登陆成功"))
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