/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.RMusicServer
import cn.rtast.rmusic.entity.config.ServerConfig
import cn.rtast.rmusic.entity.payload.inbound.*
import cn.rtast.rmusic.entity.payload.side.Pause2Side
import cn.rtast.rmusic.entity.payload.side.ResumePlay2Side
import cn.rtast.rmusic.entity.payload.side.StopPlay2Side
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.enums.MusicPlatform
import cn.rtast.rmusic.util.mc.Renderer
import cn.rtast.rmusic.util.mc.createActionPacket
import cn.rtast.rmusic.util.mc.sendToServer
import cn.rtast.rmusic.util.str.supplier
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val shortNode = dispatcher.register(
            CommandManager.literal("rm")
                .then(
                    CommandManager.literal("search")
                        .then(
                            CommandManager.literal("163")
                                .then(
                                    CommandManager.argument("keyword", StringArgumentType.string())
                                        .executes { context ->
                                            val keyword = context.getArgument("keyword", String::class.java)
                                            SearchPayloadInbound(keyword, MusicPlatform.Netease)
                                                .createActionPacket(IntentAction.SEARCH)
                                                .sendToServer()
                                        }
                                )
                        ).then(
                            CommandManager.literal("qq")
                                .then(
                                    CommandManager.argument("qq-keyword", StringArgumentType.string())
                                        .executes { context ->
                                            val keyword = context.getArgument("qq-keyword", String::class.java)
                                            SearchPayloadInbound(keyword, MusicPlatform.QQ)
                                                .createActionPacket(IntentAction.SEARCH)
                                                .sendToServer()
                                        }
                                )
                        )

                ).then(
                    CommandManager.literal("play")
                        .then(
                            CommandManager.argument("songId", StringArgumentType.string()).executes { context ->
                                val songId = context.getArgument("songId", String::class.java)
                                PlayMusicInbound(songId, MusicPlatform.Netease).createActionPacket(IntentAction.PLAY)
                                    .sendToServer()
                            }
                        ).then(
                            CommandManager.literal("qq")
                                .then(
                                    CommandManager.argument("qq-song-id", StringArgumentType.string())
                                        .executes { context ->
                                            val songId = context.getArgument("qq-song-id", String::class.java)
                                            PlayMusicInbound(
                                                songId,
                                                MusicPlatform.QQ
                                            ).createActionPacket(IntentAction.PLAY)
                                                .sendToServer()
                                        }
                                )
                        )
                ).then(
                    CommandManager.literal("pause")
                        .executes { _ ->
                            Pause2Side().createActionPacket(IntentAction.PAUSE).sendToServer()
                        }
                ).then(
                    CommandManager.literal("resume")
                        .executes { _ ->
                            ResumePlay2Side().createActionPacket(IntentAction.RESUME).sendToServer()
                        }
                ).then(
                    CommandManager.literal("stop")
                        .executes { _ ->
                            StopPlay2Side().createActionPacket(IntentAction.STOP).sendToServer()
                        }
                ).then(
                    CommandManager.literal("login").requires { it.hasPermissionLevel(3) }
                        .executes { _ ->
                            LoginInbound().createActionPacket(IntentAction.LOGIN).sendToServer()
                        }.then(
                            CommandManager.literal("confirm")
                                .then(
                                    CommandManager.argument("qrcodeKey", StringArgumentType.string())
                                        .executes { context ->
                                            val key = context.getArgument("qrcodeKey", String::class.java)
                                            Renderer.loadQRCode = false
                                            ConfirmQRCodeInbound(key).createActionPacket(IntentAction.CONFIRM_QRCODE)
                                                .sendToServer()
                                        }
                                )
                        )
                ).then(
                    CommandManager.literal("config").requires { it.hasPermissionLevel(3) }
                        .then(
                            CommandManager.literal("set-api")
                                .then(
                                    CommandManager.literal("qq-api")
                                        .then(
                                            CommandManager.argument("host", StringArgumentType.string())
                                                .executes { context ->
                                                    try {
                                                        val apiHost = context.getArgument("host", String::class.java)
                                                        val afterApiHost =
                                                            if (apiHost.endsWith("/")) apiHost.dropLast(1) else apiHost
                                                        val currentConfig = RMusicServer.configManager.read()
                                                        val afterConfig =
                                                            ServerConfig(currentConfig.api, afterApiHost)
                                                        SetConfigInbound(afterConfig).createActionPacket(IntentAction.SET_CONFIG)
                                                            .sendToServer()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    0
                                                }
                                        )
                                ).then(
                                    CommandManager.literal("163-api")
                                        .then(
                                            CommandManager.argument("apiHost", StringArgumentType.string())
                                                .executes { context ->
                                                    try {
                                                        val apiHost = context.getArgument("apiHost", String::class.java)
                                                        val afterApiHost =
                                                            if (apiHost.endsWith("/")) apiHost.dropLast(1) else apiHost
                                                        val currentConfig = RMusicServer.configManager.read()
                                                        val afterConfig =
                                                            ServerConfig(afterApiHost, currentConfig.qqApi)
                                                        SetConfigInbound(afterConfig).createActionPacket(IntentAction.SET_CONFIG)
                                                            .sendToServer()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    0
                                                }
                                        )
                                )
                        )
                ).then(
                    CommandManager.literal("clean-cache")
                        .then(
                            CommandManager.literal("confirm")
                                .executes { context ->
                                    scope.launch {
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
                                                    }).append("个文件").supplier(), false
                                        )
                                    }
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
                                    }).append("来确认操作").supplier(), false
                            )
                            0
                        }
                ).then(
                    CommandManager.literal("reload").requires { it.hasPermissionLevel(3) }
                        .executes { context ->
                            RMusicServer.configManager.reload()
                            context.source.sendFeedback(Text.literal("已重新加载配置文件").supplier(), false)
                            0
                        }
                ).then(
                    CommandManager.literal("share")
                        .then(
                            CommandManager.argument("songId", StringArgumentType.string())
                                .executes { context ->
                                    val songId = context.getArgument("songId", String::class.java)
                                    ShareMusicInbound(songId, MusicPlatform.Netease)
                                        .createActionPacket(IntentAction.SHARE)
                                        .sendToServer()
                                }
                        )
                ).then(
                    CommandManager.literal("qq-share")
                        .then(
                            CommandManager.argument("qq-song-mid", StringArgumentType.string())
                                .executes { context ->
                                    val songMid = context.getArgument("qq-song-mid", String::class.java)
                                    ShareMusicInbound(songMid, MusicPlatform.QQ)
                                        .createActionPacket(IntentAction.SHARE)
                                        .sendToServer()
                                }
                        )
                )
        )
        dispatcher.register(CommandManager.literal("rmusic").redirect(shortNode))
    }
}