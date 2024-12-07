/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.network

import cn.rtast.rmusic.RMusicServer
import cn.rtast.rmusic.entity.payload.RMusicCustomPayload
import cn.rtast.rmusic.entity.payload.inbound.*
import cn.rtast.rmusic.entity.payload.outbound.PlayMusicOutbound
import cn.rtast.rmusic.entity.payload.outbound.SearchResultOutbound
import cn.rtast.rmusic.entity.payload.outbound.ShareMusicOutbound
import cn.rtast.rmusic.entity.payload.side.Mute2Side
import cn.rtast.rmusic.entity.payload.side.Pause2Side
import cn.rtast.rmusic.entity.payload.side.ResumePlay2Side
import cn.rtast.rmusic.entity.payload.side.StopPlay2Side
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.enums.MusicPlatform
import cn.rtast.rmusic.util.mc.*
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.music.QQMusic
import cn.rtast.rmusic.util.toMinuteSecond
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text
import net.minecraft.util.Formatting

private val scope = CoroutineScope(Dispatchers.IO)

fun registerServerReceiver() {
    ServerPlayNetworking.registerGlobalReceiver(RMusicCustomPayload.ID) { payload, context ->
        try {
            val dispatchPacket = payload.decodeRawPacket()
            when (dispatchPacket.action) {
                IntentAction.LOGIN -> {
                    openMusicPlatformMenu(context.player())
                }

                IntentAction.SHARE -> {
                    context.sendMessage(Text.literal("正在准备一些必要的信息用于分享给其他玩家"))
                    val sharePacket = dispatchPacket.decode<ShareMusicInbound>()
                    when (sharePacket.musicPlatform) {
                        MusicPlatform.Netease -> {
                            scope.launch {
                                val songUrl = NCMusic.getSongUrl(sharePacket.id.toLong())
                                val songDetail = NCMusic.getSongDetail(sharePacket.id.toLong())
                                ShareMusicOutbound(
                                    songDetail.name,
                                    sharePacket.id,
                                    songDetail.artists,
                                    songUrl,
                                    context.player().name.string,
                                    MusicPlatform.Netease
                                ).createActionPacket(IntentAction.SHARE).sendToClient(context)
                            }
                        }

                        MusicPlatform.QQ -> {
                            scope.launch {
                                val songUrl = QQMusic.getSongUrl(sharePacket.id)
                                val songDetail = QQMusic.getSongInfo(sharePacket.id)
                                ShareMusicOutbound(
                                    songDetail.name,
                                    sharePacket.id,
                                    songDetail.artists,
                                    songUrl,
                                    context.player().name.string,
                                    MusicPlatform.QQ
                                ).createActionPacket(IntentAction.SHARE).sendToClient(context)
                            }
                        }
                    }
                }

                IntentAction.PLAY -> {
                    val playPacket = dispatchPacket.decode<PlayMusicInbound>()
                    when (playPacket.platform) {
                        MusicPlatform.Netease -> {
                            scope.launch {
                                val songUrl = NCMusic.getSongUrl(playPacket.id.toLong())
                                val songDetail = NCMusic.getSongDetail(playPacket.id.toLong())
                                val lyric = NCMusic.getLyric(playPacket.id.toLong())
                                PlayMusicOutbound(
                                    songUrl, playPacket.id, songDetail.name,
                                    songDetail.artists, songDetail.cover, lyric, songDetail.duration.toMinuteSecond()
                                ).createActionPacket(IntentAction.PLAY).sendToClient(context)
                            }
                        }

                        MusicPlatform.QQ -> {
                            scope.launch {
                                val songUrl = QQMusic.getSongUrl(playPacket.id)
                                val songDetail = QQMusic.getSongInfo(playPacket.id)
                                val lyric = QQMusic.getLyric(playPacket.id)
                                PlayMusicOutbound(
                                    songUrl, playPacket.id, songDetail.name,
                                    songDetail.artists, songDetail.cover, lyric, songDetail.duration.toMinuteSecond()
                                ).createActionPacket(IntentAction.PLAY).sendToClient(context)
                            }
                        }
                    }
                }

                IntentAction.STOP -> {
                    StopPlay2Side().createActionPacket(IntentAction.STOP).sendToClient(context)
                }

                IntentAction.PAUSE -> {
                    Pause2Side().createActionPacket(IntentAction.PAUSE).sendToClient(context)
                }

                IntentAction.RESUME -> {
                    ResumePlay2Side().createActionPacket(IntentAction.RESUME).sendToClient(context)
                }

                IntentAction.MUTE -> {
                    dispatchPacket.decode<Mute2Side>().createActionPacket(IntentAction.MUTE).sendToClient(context)
                }

                IntentAction.SEARCH -> {
                    val searchPacket = dispatchPacket.decode<SearchPayloadInbound>()
                    context.sendMessage(Text.literal("正在搜索: ").append(Text.literal(searchPacket.keyword).styled {
                        it.withColor(Formatting.YELLOW)
                    }))
                    when (searchPacket.searchPlatform) {
                        MusicPlatform.Netease -> {
                            scope.launch {
                                val result = NCMusic.search(searchPacket.keyword).take(8)
                                SearchResultOutbound(result.map {
                                    SearchResultOutbound.Result(
                                        it.id,
                                        it.name,
                                        it.artists,
                                        MusicPlatform.Netease
                                    )
                                }).createActionPacket(IntentAction.SEARCH).sendToClient(context)
                            }
                        }

                        MusicPlatform.QQ -> {
                            scope.launch {
                                val result = QQMusic.search(searchPacket.keyword).take(8)
                                SearchResultOutbound(result.map {
                                    SearchResultOutbound.Result(
                                        it.id,
                                        it.name,
                                        it.artists,
                                        MusicPlatform.QQ
                                    )
                                }).createActionPacket(IntentAction.SEARCH).sendToClient(context)
                            }
                        }
                    }
                }

                IntentAction.SET_CONFIG -> {
                    val config = dispatchPacket.decode<SetConfigInbound>().config
                    RMusicServer.configManager.write(config)
                    context.sendMessage(Text.literal("成功更新配置文件"))
                }

                IntentAction.CONFIRM_QRCODE -> {
                    val packet = dispatchPacket.decode<ConfirmQRCodeInbound>()
                    scope.launch {
                        context.sendMessage(Text.literal("正在检查登录状态中"))
                        try {
                            val cookie = NCMusic.checkQRCodeStatus(packet.key)
                            if (cookie != null) {
                                RMusicServer.loginManager.login(cookie)
                                val accountInfo = NCMusic.getUserAccount()
                                context.sendMessage(Text.literal("登陆成功, 用户名: $accountInfo"))
                            } else {
                                context.sendMessage(Text.literal("验证失败, 请检查是否扫码并成功登录"))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            context.sendMessage(Text.literal("验证失败, 错误原因: ${e.message}"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}