/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.RMusicClient
import cn.rtast.rmusic.entity.Config
import cn.rtast.rmusic.entity.payload.RMusicPayload
import cn.rtast.rmusic.entity.payload.ShareMusicPacket
import cn.rtast.rmusic.enums.Action
import cn.rtast.rmusic.enums.LyricPosition
import cn.rtast.rmusic.network.minecraftClient
import cn.rtast.rmusic.qrcodeId
import cn.rtast.rmusic.util.Renderer
import cn.rtast.rmusic.util.createActionPacket
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.str.toJson
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.io.File

class RMusicCommand : ClientCommandRegistrationCallback {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("rm")
                .then(
                    ClientCommandManager.literal("search")
                        .then(
                            argument("keyword", StringArgumentType.string()).executes { context ->
                                val keyword = context.getArgument("keyword", String::class.java)
                                context.source.sendFeedback(
                                    Text.translatable("rmusic.searching").append(Text.literal(keyword).styled {
                                        it.withColor(Formatting.YELLOW)
                                    })
                                )
                                scope.launch {
                                    try {
                                        val result = NCMusic.search(keyword)
                                        val text = Text.translatable("rmusic.searched_result")
                                        context.source.sendFeedback(text)
                                        result.take(5).forEach { r ->
                                            val songText =
                                                Text.translatable("rmusic.search.song_name").styled {
                                                    it.withColor(Formatting.GREEN)
                                                }.append(Text.literal("${r.name} ").styled {
                                                    it.withColor(Formatting.AQUA)
                                                }).append(Text.translatable("rmusic.search.artists"))
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
                                                            Text.translatable("rmusic.click_to_play_hover")
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
                                                            Text.translatable("rmusic.click_to_share_hover")
                                                                .append(
                                                                    Text.translatable("rmusic.click_to_share_hover_tip")
                                                                        .styled {
                                                                            it.withColor(Formatting.RED)
                                                                        })
                                                        )
                                                    )
                                            }
                                            songText.append(shareButton)
                                            context.source.sendFeedback(songText)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                0
                            }
                        )
                ).then(
                    ClientCommandManager.literal("play")
                        .then(
                            argument("songId", LongArgumentType.longArg(0)).executes { context ->
                                if (RMusicClient.player.isOpened || RMusicClient.player.isPausedOrPlaying) {
                                    context.source.sendFeedback(
                                        Text.translatable("rmusic.is_already_playin")
                                            .append(Text.translatable("rmusic.here").styled { style ->
                                                style.withColor(Formatting.GREEN)
                                                    .withHoverEvent(
                                                        HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Text.translatable("rmusic.click_to_stop_current_song_hover")
                                                                .styled {
                                                                    it.withColor(Formatting.YELLOW)
                                                                })
                                                    )
                                                    .withClickEvent(
                                                        ClickEvent(
                                                            ClickEvent.Action.RUN_COMMAND,
                                                            "/rm stop"
                                                        )
                                                    )
                                            }).append(Text.translatable("rmusic.to_stop_current_song"))
                                    )
                                    return@executes 0
                                }
                                val songId = context.getArgument("songId", Long::class.java)
                                scope.launch {
                                    try {
                                        context.source.sendFeedback(Text.translatable("rmusic.downloading_song_to_local"))
                                        val songDetail = NCMusic.getSongDetail(songId)
                                        Renderer.songInfo = songDetail
                                        Renderer.registerLoadingCover()
                                        minecraftClient.inGameHud.setOverlayMessage(
                                            Text.translatable("rmusic.now_playing_actionbar")
                                                .append(Text.literal("《${songDetail.name}》 - ${songDetail.artists}")),
                                            true
                                        )
                                        Renderer.renderCover(songDetail)
                                        Renderer.renderSongDetail()
                                        val lyric = NCMusic.getLyric(songId)
                                        RMusicClient.player.playMusic(lyric, songDetail)
                                    } catch (e: NullPointerException) {
                                        e.printStackTrace()
                                        context.source.sendFeedback(Text.translatable("rmusic.play_failed"))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                0
                            }
                        )
                ).then(
                    ClientCommandManager.literal("pause")
                        .executes { context ->
                            RMusicClient.player.pause()
                            context.source.sendFeedback(Text.translatable("rmusic.paused"))
                            0
                        }
                ).then(
                    ClientCommandManager.literal("resume")
                        .executes { context ->
                            RMusicClient.player.resume()
                            context.source.sendFeedback(Text.translatable("rmusic.resumed"))
                            0
                        }
                ).then(
                    ClientCommandManager.literal("stop")
                        .executes { context ->
                            RMusicClient.player.stop()
                            context.source.sendFeedback(Text.translatable("rmusic.stoppped"))
                            0
                        }
                ).then(
                    ClientCommandManager.literal("login")
                        .executes { context ->
                            context.source.sendFeedback(Text.translatable("rmusic.get_qrcode"))
                            scope.launch {
                                try {
                                    val (key, qrcode) = NCMusic.loginByQRCode()
                                    Renderer.renderQRCode(qrcode)
                                    context.source.sendFeedback(
                                        Text.translatable("rmusic.after_scan_qrcode")
                                            .append(Text.translatable("rmusic.here").styled { style ->
                                                style.withColor(Formatting.GREEN).withHoverEvent(
                                                    HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT,
                                                        Text.translatable("rmusic.to_confirm")
                                                            .styled { it.withColor(Formatting.GREEN) }
                                                    )
                                                ).withClickEvent(
                                                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm login confirm $key")
                                                )
                                            })
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            0
                        }.then(
                            ClientCommandManager.literal("status")
                                .executes { context ->
                                    if (RMusicClient.loginManager.getCookie() != null) {
                                        context.source.sendFeedback(Text.translatable("rmusic.login_status_logged"))
                                    } else {
                                        context.source.sendFeedback(Text.translatable("rmusic.login_status_not_logged"))
                                    }
                                    0
                                }
                        ).then(
                            ClientCommandManager.literal("logout")
                                .executes { context ->
                                    RMusicClient.loginManager.logout()
                                    context.source.sendFeedback(Text.translatable("rmusic.login_status_logout"))
                                    0
                                }
                        ).then(
                            ClientCommandManager.literal("confirm")
                                .then(
                                    argument("qrcodeKey", StringArgumentType.string()).executes { context ->
                                        context.source.sendFeedback(Text.translatable("rmusic.login_status_checking"))
                                        scope.launch {
                                            try {
                                                Renderer.loadQRCode = false
                                                Renderer.destroyTexture(qrcodeId)
                                                val qrCodeKey = context.getArgument("qrcodeKey", String::class.java)
                                                val cookie = NCMusic.checkQRCodeStatus(qrCodeKey)
                                                if (cookie != null) {
                                                    RMusicClient.loginManager.login(cookie)
                                                    val accountInfo = NCMusic.getUserAccount()
                                                    context.source.sendFeedback(
                                                        Text.translatable(
                                                            "rmusic.login_status_checking_success",
                                                            accountInfo
                                                        )
                                                    )
                                                } else {
                                                    context.source.sendFeedback(Text.translatable("rmusic.login_status_checking_failed"))
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
                    ClientCommandManager.literal("config")
                        .then(
                            ClientCommandManager.literal("set-api")
                                .then(
                                    argument("apiHost", StringArgumentType.string())
                                        .executes { context ->
                                            try {
                                                val apiHost = context.getArgument("apiHost", String::class.java)
                                                val afterApiHost =
                                                    if (apiHost.endsWith("/")) apiHost.dropLast(1) else apiHost
                                                val currentConfig = RMusicClient.configManager.read()
                                                val afterConfig =
                                                    Config(
                                                        afterApiHost,
                                                        currentConfig.autoPause,
                                                        currentConfig.qqMusicApi,
                                                        currentConfig.position
                                                    )
                                                RMusicClient.configManager.write(afterConfig)
                                                context.source.sendFeedback(
                                                    Text.translatable(
                                                        "rmusic.set_api_host",
                                                        apiHost
                                                    )
                                                )
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            0
                                        }
                                )
                        ).then(
                            ClientCommandManager.literal("default")
                                .executes { context ->
                                    RMusicClient.configManager.default()
                                    context.source.sendFeedback(Text.translatable("rmusic.set_default_config"))
                                    0
                                }
                        ).then(
                            ClientCommandManager.literal("auto-pause")
                                .then(
                                    argument("autoPause", BoolArgumentType.bool())
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
                                                Text.translatable(
                                                    "rmusic.set_auto_paused",
                                                    settings
                                                )
                                            )
                                            0
                                        }
                                )
                        ).then(
                            ClientCommandManager.literal("lyric-position")
                                .then(
                                    argument("lyric-position", StringArgumentType.word())
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
                                                Text.translatable(
                                                    "rmusic.set_lyric_position",
                                                    lyricPosition.translation
                                                )
                                            )
                                            0
                                        }
                                )
                        )
                ).then(
                    ClientCommandManager.literal("clean-cache")
                        .then(
                            ClientCommandManager.literal("confirm")
                                .executes { context ->
                                    val files = File("./config/rmusic/cache").listFiles()
                                        ?.filter { it.isFile } ?: emptyList()
                                    files.forEach { file -> file.delete() }
                                    context.source.sendFeedback(
                                        Text.translatable("rmusic.deleted").append(
                                            Text.literal("${files.size}")
                                                .styled {
                                                    it.withColor(Formatting.RED)
                                                        .withHoverEvent(
                                                            HoverEvent(
                                                                HoverEvent.Action.SHOW_TEXT,
                                                                Text.literal(files.joinToString(separator = "\n") { file -> file.name })
                                                            )
                                                        )
                                                }).append(Text.translatable("rmusic.files"))
                                    )
                                    0
                                }
                        )
                        .executes { context ->
                            context.source.sendFeedback(
                                Text.translatable("rmusic.clean_cache").append(
                                    Text.translatable("rmusic.here").styled { style ->
                                        style.withColor(Formatting.GREEN)
                                            .withHoverEvent(
                                                HoverEvent(
                                                    HoverEvent.Action.SHOW_TEXT,
                                                    Text.translatable("rmusic.click_to_confirm_hover").styled {
                                                        it.withColor(Formatting.GREEN)
                                                    })
                                            )
                                            .withClickEvent(
                                                ClickEvent(
                                                    ClickEvent.Action.RUN_COMMAND,
                                                    "/rm clean-cache confirm"
                                                )
                                            )
                                    }).append(Text.translatable("rmusic.click_to_confirm"))
                            )
                            0
                        }
                ).then(
                    ClientCommandManager.literal("reload")
                        .executes { context ->
                            RMusicClient.configManager.reload()
                            context.source.sendFeedback(Text.translatable("rmusic.reloaded_config"))
                            0
                        }
                ).then(
                    ClientCommandManager.literal("share")
                        .then(
                            argument("songId", LongArgumentType.longArg())
                                .executes { context ->
                                    val songId = context.getArgument("songId", Long::class.java)
                                    context.source.sendFeedback(Text.translatable("rmusic.preparing_data_to_share_music"))
                                    scope.launch {
                                        try {
                                            val detail = NCMusic.getSongDetail(songId)
                                            val songUrl = NCMusic.getSongUrl(songId)
                                            val musicSharePayload = ShareMusicPacket(
                                                detail.name,
                                                songId,
                                                detail.artists,
                                                songUrl,
                                                context.source.player.name.string
                                            ).toJson()
                                            val actionPacket = createActionPacket(Action.SHARE, musicSharePayload)
                                            val packet = RMusicPayload(actionPacket)
                                            ClientPlayNetworking.send(packet)
                                            context.source.sendFeedback(Text.translatable("rmusic.success_shared_to_other_players"))
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                    0
                                }
                        )
                ).then(
                    ClientCommandManager.literal("qq-search")
                        .then(
                            argument("qq-search-keyword", StringArgumentType.string())
                                .executes { context ->

                                    0
                                }
                        )
                )
        )
    }
}