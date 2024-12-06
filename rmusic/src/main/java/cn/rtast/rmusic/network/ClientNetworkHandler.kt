/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.network

import cn.rtast.rmusic.RMusicClient
import cn.rtast.rmusic.entity.payload.RMusicCustomPayload
import cn.rtast.rmusic.entity.payload.outbound.PlayMusicOutbound
import cn.rtast.rmusic.entity.payload.outbound.QRCodeLoginOutbound
import cn.rtast.rmusic.entity.payload.outbound.SearchResultOutbound
import cn.rtast.rmusic.entity.payload.outbound.ShareMusicOutbound
import cn.rtast.rmusic.entity.payload.side.Mute2Side
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.qrcodeId
import cn.rtast.rmusic.util.*
import cn.rtast.rmusic.util.str.decodeToByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.net.URI

private val minecraftClient: MinecraftClient = MinecraftClient.getInstance()
private val scope = CoroutineScope(Dispatchers.IO)

fun registerClientReceiver() {
    ClientPlayNetworking.registerGlobalReceiver(RMusicCustomPayload.ID) { payload, context ->
        val dispatchPacket = payload.decodeRawPacket()
        when (dispatchPacket.action) {
            IntentAction.LOGIN -> {
                val loginPacket = dispatchPacket.decode<QRCodeLoginOutbound>()
                Renderer.renderQRCode(loginPacket.qrcodeBase64.decodeToByteArray())
                try {
                    context.player().sendMessage(
                        Text.literal("扫码并登录完成后点击")
                            .append(Text.literal("[这里]").styled { style ->
                                style.withColor(Formatting.GREEN).withHoverEvent(
                                    HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        Text.literal("点击这里确认登录状态")
                                            .styled { it.withColor(Formatting.GREEN) }
                                    )
                                ).withClickEvent(
                                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm login confirm ${loginPacket.key}")
                                )
                            }), false
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            IntentAction.SHARE -> {
                minecraftClient.player?.playSoundToPlayer(
                    SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(),
                    SoundCategory.BLOCKS, 1f, 1f
                )
                val sharePacket = dispatchPacket.decode<ShareMusicOutbound>()
                val musicText = Text.literal("玩家 ").append(Text.literal(sharePacket.fromWho).styled {
                    it.withColor(Formatting.YELLOW)
                }).append("向你分享了一首歌: ").append("《${sharePacket.name}》- ${sharePacket.artists}")
                    .styled { it.withColor(Formatting.AQUA) }
                    .append("点击").append(Text.literal("[这里]").styled {
                        it.withColor(Formatting.GREEN)
                            .withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Text.literal("点击播放: 《${sharePacket.name}》- ${sharePacket.artists}")
                                )
                            ).withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm play ${sharePacket.id}"))
                    }).append(Text.literal("来播放这首歌"))
                context.sendMessage(musicText, false)
            }

            IntentAction.PLAY -> {
                val playPacket = dispatchPacket.decode<PlayMusicOutbound>()
                if (RMusicClient.player.isOpened || RMusicClient.player.isPausedOrPlaying) {
                    context.sendMessage(
                        Text.literal("你已经在播放歌曲啦!, 你可以点击")
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
                            }).append(Text.literal("来停止播放当前歌曲"))
                    )
                } else {
                    scope.launch {
                        try {
                            context.sendMessage(Text.literal("正在下载歌曲到本地..."))
                            Renderer.registerLoadingCover()
                            minecraftClient.inGameHud.setOverlayMessage(
                                Text.literal("正在播放: ")
                                    .append(Text.literal("《${playPacket.songName}》by: ${playPacket.artistName}"))
                                    .append(
                                        Text.literal(" - ${playPacket.duration}")),
                                true
                            )
                            val coverBytes = URI("${playPacket.cover}?param=128x128").toURL()
                                .readBytes().toPNG().cropToCircle()
                                .toBufferedImage().createRecordImage().toByteArray()
                            Renderer.renderCover(coverBytes)
                            Renderer.renderSongDetail()
                            RMusicClient.player.playMusic(
                                playPacket.lyric,
                                playPacket.songName,
                                playPacket.artistName,
                                playPacket.id,
                                playPacket.url
                            )
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                            context.sendMessage(Text.literal("播放音乐失败(可能是没有登陆播放了付费歌曲)"))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            IntentAction.STOP -> {
                RMusicClient.player.stop()
                context.sendMessage(Text.literal("已停止播放音乐"))
            }

            IntentAction.PAUSE -> {
                RMusicClient.player.pause()
                context.sendMessage(Text.literal("已暂停播放音乐"))
            }

            IntentAction.RESUME -> {
                RMusicClient.player.resume()
                context.sendMessage(Text.literal("已恢复播放音乐"))
            }

            IntentAction.MUTE -> {
                val mutePacket = dispatchPacket.decode<Mute2Side>()
                RMusicClient.player.mute = mutePacket.isMuted
                context.sendMessage(Text.literal("成功设置静音状态为: ${mutePacket.isMuted}"))
            }

            IntentAction.SEARCH -> {
                val searchResult = dispatchPacket.decode<SearchResultOutbound>().result
                try {
                    val text = Text.literal("搜索到如下结果:")
                    context.sendMessage(text)
                    searchResult.forEach { r ->
                        val songText =
                            Text.literal("歌曲名: ").styled {
                                it.withColor(Formatting.GREEN)
                            }.append(Text.literal("${r.songName} ").styled {
                                it.withColor(Formatting.AQUA)
                            }).append(" 歌手: ")
                                .append(Text.literal(r.artistName).styled {
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
                                ).withHoverEvent(
                                    HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        Text.literal("点击分享给服务器内的其他玩家\n")
                                            .append(
                                                Text.literal("注意: 只有服务器安装了RMusic并且其他玩家安装了RMusic客户端模组的玩家才能收到分享消息")
                                                    .styled { it.withColor(Formatting.RED) })
                                    )
                                )
                        }
                        songText.append(shareButton)
                        context.sendMessage(songText)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            IntentAction.SET_CONFIG -> {
                TODO("unreachable")
            }

            IntentAction.CONFIRM_QRCODE -> {
                Renderer.loadQRCode = false
                Renderer.destroyTexture(qrcodeId)
            }
        }
    }
}