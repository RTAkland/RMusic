/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.network

import cn.rtast.rmusic.RMusicClient
import cn.rtast.rmusic.defaultCoverId
import cn.rtast.rmusic.entity.ncm.SongDetail
import cn.rtast.rmusic.entity.payload.*
import cn.rtast.rmusic.enums.DispatchAction
import cn.rtast.rmusic.util.renderCover
import cn.rtast.rmusic.util.renderSongDetail
import cn.rtast.rmusic.util.songInfo
import cn.rtast.rmusic.util.str.decodeToString
import cn.rtast.rmusic.util.str.fromJson
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting

val minecraftClient: MinecraftClient = MinecraftClient.getInstance()

fun registerClientReceiver() {
    ClientPlayNetworking.registerGlobalReceiver(ShareMusicCustomPayload.ID) { payload, context ->
        minecraftClient.player?.playSoundToPlayer(
            SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(),
            SoundCategory.BLOCKS, 1f, 1f
        )
        val dispatchPacket = payload.payload.decodeToString().fromJson<Action2SidePacket>()
        when (dispatchPacket.action) {
            DispatchAction.PLAY_MUSIC -> {
                if (RMusicClient.player.isOpened || RMusicClient.player.isPausedOrPlaying) {
                    context.player().sendMessage(
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
                            }).append(Text.literal("来停止播放当前歌曲")), false
                    )
                } else {
                    val packet = dispatchPacket.body.decodeToString().fromJson<PlayMusicPacket>()
                    try {
                        context.player().sendMessage(Text.literal("正在下载歌曲到本地..."), false)
                        val songDetail = SongDetail(packet.songName, packet.id, packet.cover, packet.artists)
                        songInfo = songDetail
                        minecraftClient.textureManager.registerTexture(
                            defaultCoverId, minecraftClient.textureManager.getTexture(defaultCoverId)
                        )
                        renderCover(songDetail)
                        renderSongDetail()
                        RMusicClient.player.playMusic(packet.lyric, songDetail)
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                        context.player().sendMessage(
                            Text.literal("播放音乐失败(可能是没有登陆播放了付费歌曲)"), false
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            DispatchAction.STOP_MUSIC -> {
                RMusicClient.player.stop()
                context.player()
                    .sendMessage(Text.literal("已停止播放").styled {
                        it.withColor(Formatting.GREEN)
                    }, false)
            }

            DispatchAction.RESUME_MUSIC -> {
                RMusicClient.player.resume()
                context.player().sendMessage(Text.literal("继续播放音乐").styled {
                    it.withColor(Formatting.GREEN)
                }, false)
            }

            DispatchAction.SEARCH_MUSIC -> {
                val packet = dispatchPacket.body.decodeToString().fromJson<SearchResultPacket>()
                try {
                    val text = Text.literal("搜索到如下结果:")
                    context.player().sendMessage(text, false)
                    packet.result.take(5).forEach { r ->
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
                        context.player().sendMessage(songText, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            DispatchAction.SHARE_MUSIC -> {
                val packet = dispatchPacket.body.decodeToString().fromJson<ShareMusicPacket>()
                val musicText = Text.literal("玩家 ").append(Text.literal(packet.fromWho).styled {
                    it.withColor(Formatting.YELLOW)
                }).append("向你分享了一首歌: ").append("《${packet.name}》- ${packet.artists}")
                    .styled { it.withColor(Formatting.AQUA) }
                    .append("点击").append(Text.literal("[这里]").styled {
                        it.withColor(Formatting.GREEN)
                            .withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Text.literal("点击播放: 《${packet.name}》- ${packet.artists}")
                                )
                            ).withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm play ${packet.id}"))
                    }).append(Text.literal("来播放这首歌"))
                context.player()?.sendMessage(musicText, false)
            }

            DispatchAction.PAUSE_MUSIC -> {
                RMusicClient.player.pause()
                context.player().sendMessage(Text.literal("已暂停播放音乐").styled {
                    it.withColor(Formatting.GREEN)
                }, false)
            }
        }
    }
}