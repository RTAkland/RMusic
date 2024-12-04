/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.RMusicClient
import cn.rtast.rmusic.defaultCoverId
import cn.rtast.rmusic.entity.Config
import cn.rtast.rmusic.entity.MusicPayload
import cn.rtast.rmusic.entity.payload.ShareMusicCustomPayload
import cn.rtast.rmusic.entity.payload.ShareMusicPacket
import cn.rtast.rmusic.enums.LyricPosition
import cn.rtast.rmusic.network.minecraftClient
import cn.rtast.rmusic.qrcodeId
import cn.rtast.rmusic.util.*
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.str.encodeToBase64
import cn.rtast.rmusic.util.str.toJson
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.io.File

class RMusicCommand : CommandRegistrationCallback {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        dispatcher.register(
            CommandManager.literal("rm")
                .then(
                    CommandManager.literal("search")
                        .then(
                            CommandManager.argument("keyword", StringArgumentType.string()).executes { context ->
                                val keyword = context.getArgument("keyword", String::class.java)
                                context.source.sendFeedback(
                                    Text.literal("正在搜索: ").append(Text.literal(keyword).styled {
                                        it.withColor(Formatting.YELLOW)
                                    }).createSupplierText(), false
                                )
                                scope.launch {
                                    try {
                                        val result = NCMusic.search(keyword)
                                        val text = Text.literal("搜索到如下结果:")
                                        context.source.sendFeedback(text.createSupplierText(), false)
                                        result.take(5).forEach { r ->
                                            val songText =
                                                Text.literal("歌曲名: ").styled {
                                                    it.withColor(Formatting.GREEN)
                                                }.append(Text.literal("${r.name} ").styled {
                                                    it.withColor(Formatting.AQUA)
                                                }).append(" 歌手: ")
                                                    .append(Text.literal(r.artists).styled {
                                                        it.withColor(Formatting.AQUA)
                                                    })
                                            val playButton = Text.literal(" [▶]").styled { style ->
                                                style.withColor(Formatting.DARK_PURPLE)
                                                    .withClickEvent(
                                                        ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm play ${r.id}")
                                                    )
                                                    .withHoverEvent(
                                                        HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Text.literal("点击播放音乐")
                                                                .styled { it.withColor(Formatting.YELLOW) })
                                                    )
                                            }
                                            songText.append(playButton)
                                            val shareButton = Text.literal(" [↗]").styled { style ->
                                                style.withColor(Formatting.AQUA)
                                                    .withClickEvent(
                                                        ClickEvent(
                                                            ClickEvent.Action.RUN_COMMAND,
                                                            "/rm share ${r.id}"
                                                        )
                                                    )
                                                    .withHoverEvent(
                                                        HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Text.literal("点击分享给服务器内的其他玩家\n")
                                                                .append(
                                                                    Text.literal("注意: 只有服务器安装了RMusic并且其他玩家安装了RMusic客户端模组的玩家才能收到分享消息")
                                                                        .styled {
                                                                            it.withColor(Formatting.RED)
                                                                        })
                                                        )
                                                    )
                                            }
                                            songText.append(shareButton)
                                            context.source.sendFeedback(songText.createSupplierText(), false)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                0
                            }
                        )
                ).then(
                    CommandManager.literal("play")
                        .then(
                            CommandManager.argument("songId", LongArgumentType.longArg(0)).executes { context ->
                                if (RMusicClient.player.isOpened || RMusicClient.player.isPausedOrPlaying) {
                                    context.source.sendFeedback(
                                        Text.literal("你已经在播放歌曲啦!, 你可以点")
                                            .append(Text.literal("[这里]").styled { style ->
                                                style.withColor(Formatting.GREEN)
                                                    .withHoverEvent(
                                                        HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Text.literal("点击停止播放当前正在播放的歌曲").styled {
                                                                it.withColor(Formatting.YELLOW)
                                                            })
                                                    )
                                                    .withClickEvent(
                                                        ClickEvent(
                                                            ClickEvent.Action.RUN_COMMAND,
                                                            "/rm stop"
                                                        )
                                                    )
                                            }).append(Text.literal("来停止播放当前歌曲")).createSupplierText(), false
                                    )
                                    return@executes 0
                                }
                                val songId = context.getArgument("songId", Long::class.java)
                                scope.launch {
                                    try {
                                        context.source.sendFeedback(
                                            Text.literal("正在下载歌曲到本地...").createSupplierText(), false
                                        )
                                        val songDetail = NCMusic.getSongDetail(songId)
                                        songInfo = songDetail
                                        minecraftClient.textureManager.registerTexture(
                                            defaultCoverId, minecraftClient.textureManager.getTexture(defaultCoverId)
                                        )
                                        renderCover(songDetail)
                                        renderSongDetail()
                                        val lyric = NCMusic.getLyric(songId)
                                        RMusicClient.player.playMusic(lyric, songDetail)
                                    } catch (e: NullPointerException) {
                                        e.printStackTrace()
                                        context.source.sendFeedback(
                                            Text.literal("播放音乐失败(可能是没有登陆播放了付费歌曲)")
                                                .createSupplierText(), false
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                0
                            }
                        )
                ).then(
                    CommandManager.literal("pause")
                        .executes { context ->
                            RMusicClient.player.pause()
                            context.source.sendFeedback(Text.literal("已暂停").createSupplierText(), false)
                            0
                        }
                ).then(
                    CommandManager.literal("resume")
                        .executes { context ->
                            RMusicClient.player.resume()
                            context.source.sendFeedback(Text.literal("已恢复播放").createSupplierText(), false)
                            0
                        }
                ).then(
                    CommandManager.literal("stop")
                        .executes { context ->
                            RMusicClient.player.stop()
                            context.source.sendFeedback(Text.literal("已停止播放").createSupplierText(), false)
                            0
                        }
                ).then(
                    CommandManager.literal("login")
                        .executes { context ->
                            context.source.sendFeedback(Text.literal("正在获取二维码...").createSupplierText(), false)
                            scope.launch {
                                try {
                                    loadQRCode = false
                                    val (key, qrcode) = NCMusic.loginByQRCode()
                                    println(key)
                                    renderQRCode(qrcode)
                                    context.source.sendFeedback(
                                        Text.literal("扫码并登录完成后点击")
                                            .append(Text.literal("[这里]").styled { style ->
                                                style.withColor(Formatting.GREEN).withHoverEvent(
                                                    HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT,
                                                        Text.literal("点击这里确认登录状态")
                                                            .styled { it.withColor(Formatting.GREEN) }
                                                    )
                                                ).withClickEvent(
                                                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm login confirm $key")
                                                )
                                            }).createSupplierText(), false
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            0
                        }.then(
                            CommandManager.literal("status")
                                .executes { context ->
                                    if (RMusicClient.loginManager.getCookie() != null) {
                                        context.source.sendFeedback(
                                            Text.literal("你已登录").createSupplierText(),
                                            false
                                        )
                                    } else {
                                        context.source.sendFeedback(
                                            Text.literal("你还未登录").createSupplierText(),
                                            false
                                        )
                                    }
                                    0
                                }
                        ).then(
                            CommandManager.literal("logout")
                                .executes { context ->
                                    RMusicClient.loginManager.logout()
                                    context.source.sendFeedback(Text.literal("已退出登录").createSupplierText(), false)
                                    0
                                }
                        ).then(
                            CommandManager.literal("confirm")
                                .then(
                                    CommandManager.argument("qrcodeKey", StringArgumentType.string())
                                        .executes { context ->
                                            context.source.sendFeedback(
                                                Text.literal("正在检查登录状态中").createSupplierText(), false
                                            )
                                            scope.launch {
                                                try {
                                                    loadQRCode = false
                                                    destroyTexture(qrcodeId)
                                                    val qrCodeKey = context.getArgument("qrcodeKey", String::class.java)
                                                    val cookie = NCMusic.checkQRCodeStatus(qrCodeKey)
                                                    if (cookie != null) {
                                                        RMusicClient.loginManager.login(cookie)
                                                        val accountInfo = NCMusic.getUserAccount()
                                                        context.source.sendFeedback(
                                                            Text.literal("登陆成功, 用户名: $accountInfo")
                                                                .createSupplierText(), false
                                                        )
                                                    } else {
                                                        context.source.sendFeedback(
                                                            Text.literal("验证失败, 请检查是否扫码并成功登录")
                                                                .createSupplierText(), false
                                                        )
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }
                                            }
                                            0
                                        }
                                )
                        )
                ).then(
                    CommandManager.literal("config")
                        .then(
                            CommandManager.literal("set-api")
                                .then(
                                    CommandManager.argument("apiHost", StringArgumentType.string())
                                        .executes { context ->
                                            try {
                                                val apiHost = context.getArgument("apiHost", String::class.java)
                                                val currentConfig = RMusicClient.configManager.read()
                                                val afterConfig =
                                                    Config(
                                                        apiHost,
                                                        currentConfig.autoPause,
                                                        currentConfig.qqMusicApi,
                                                        currentConfig.position
                                                    )
                                                RMusicClient.configManager.write(afterConfig)
                                                context.source.sendFeedback(
                                                    Text.literal("设置api地址成功: $apiHost").createSupplierText(),
                                                    false
                                                )
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            0
                                        }
                                )
                        ).then(
                            CommandManager.literal("default")
                                .executes { context ->
                                    RMusicClient.configManager.default()
                                    context.source.sendFeedback(
                                        Text.literal("已将配置文件设置为默认").createSupplierText(), false
                                    )
                                    0
                                }
                        ).then(
                            CommandManager.literal("auto-pause")
                                .then(
                                    CommandManager.argument("autoPause", BoolArgumentType.bool())
                                        .executes { context ->
                                            val settings = context.getArgument("autoPause", Boolean::class.java)
                                            val currentConfig = RMusicClient.configManager.read()
                                            val afterConfig =
                                                Config(
                                                    currentConfig.api,
                                                    settings,
                                                    currentConfig.qqMusicApi,
                                                    currentConfig.position
                                                )
                                            RMusicClient.configManager.write(afterConfig)
                                            context.source.sendFeedback(
                                                Text.literal("auto-pause设置成功: $settings").createSupplierText(),
                                                false
                                            )
                                            0
                                        }
                                )
                        ).then(
                            CommandManager.literal("lyric-position")
                                .then(
                                    CommandManager.argument("lyric-position", StringArgumentType.word())
                                        .suggests { _, builder ->
                                            builder.suggest("action-bar")
                                            builder.suggest("top-left")
                                            builder.buildFuture()
                                        }
                                        .executes { context ->
                                            val rawPosition = context.getArgument("lyric-position", String::class.java)
                                            val lyricPosition = when (rawPosition) {
                                                "action-bar" -> LyricPosition.ActionBar
                                                "top-left" -> LyricPosition.TopLeft
                                                else -> LyricPosition.ActionBar
                                            }
                                            val currentConfig = RMusicClient.configManager.read()
                                            val afterConfig = Config(
                                                currentConfig.api,
                                                currentConfig.autoPause,
                                                currentConfig.qqMusicApi,
                                                lyricPosition
                                            )
                                            RMusicClient.configManager.write(afterConfig)
                                            context.source.sendFeedback(
                                                Text.literal("成功设置歌词位置为: ${lyricPosition.translation}, 需要使用/rm reload 之后播放下一首歌才能生效")
                                                    .createSupplierText(), false
                                            )
                                            0
                                        }
                                )
                        )
                ).then(
                    CommandManager.literal("clean-cache")
                        .then(
                            CommandManager.literal("confirm")
                                .executes { context ->
                                    val files = File("./config/rmusic/cache").listFiles()
                                        ?.filter { it.isFile } ?: emptyList()
                                    files.forEach { file -> file.delete() }
                                    context.source.sendFeedback(
                                        Text.literal("已删除").append(
                                            Text.literal("${files.size}")
                                                .styled {
                                                    it.withColor(Formatting.RED)
                                                        .withHoverEvent(
                                                            HoverEvent(
                                                                HoverEvent.Action.SHOW_TEXT,
                                                                Text.literal(files.joinToString(separator = "\n") { file -> file.name })
                                                            )
                                                        )
                                                }).append("个文件").createSupplierText(), false
                                    )
                                    0
                                }
                        )
                        .executes { context ->
                            context.source.sendFeedback(
                                Text.literal("本操作会清空所有已缓存到本地的音频文件, 点击").append(
                                    Text.literal("[这里]").styled { style ->
                                        style.withColor(Formatting.GREEN)
                                            .withHoverEvent(
                                                HoverEvent(
                                                    HoverEvent.Action.SHOW_TEXT,
                                                    Text.literal("点击确认").styled {
                                                        it.withColor(Formatting.GREEN)
                                                    })
                                            )
                                            .withClickEvent(
                                                ClickEvent(
                                                    ClickEvent.Action.RUN_COMMAND,
                                                    "/rm clean-cache confirm"
                                                )
                                            )
                                    }).append("来确认操作").createSupplierText(), false
                            )
                            0
                        }
                ).then(
                    CommandManager.literal("reload")
                        .executes { context ->
                            RMusicClient.configManager.reload()
                            context.source.sendFeedback(Text.literal("已重新加载配置文件").createSupplierText(), false)
                            0
                        }
                ).then(
                    CommandManager.literal("share")
                        .then(
                            CommandManager.argument("songId", LongArgumentType.longArg())
                                .executes { context ->
                                    val songId = context.getArgument("songId", Long::class.java)
                                    context.source.sendFeedback(
                                        Text.literal("正在准备一些必要的信息用于分享给其他玩家").createSupplierText(),
                                        false
                                    )
                                    scope.launch {
                                        try {
                                            val detail = NCMusic.getSongDetail(songId)
                                            val songUrl = NCMusic.getSongUrl(songId)
                                            val payload = ShareMusicPacket(
                                                detail.name,
                                                songId,
                                                detail.artists,
                                                songUrl,
                                                context.source.player?.name?.string ?: "Unknown",
                                            ).toJson().encodeToBase64()
                                            val packet = ShareMusicCustomPayload(payload)
                                            ClientPlayNetworking.send(packet)
                                            context.source.sendFeedback(
                                                Text.literal("已经成功分享到服务器内其他的玩家了").createSupplierText(),
                                                false
                                            )
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                    0
                                }
                        )
                ).then(
                    CommandManager.literal("qq-search")
                        .then(
                            CommandManager.argument("qq-search-keyword", StringArgumentType.string())
                                .executes { context ->

                                    0
                                }
                        )
                )
        )
    }
}