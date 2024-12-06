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
import cn.rtast.rmusic.util.*
import cn.rtast.rmusic.util.music.NCMusic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting

private val scope = CoroutineScope(Dispatchers.IO)

fun registerServerReceiver() {
    ServerPlayNetworking.registerGlobalReceiver(RMusicCustomPayload.ID) { payload, context ->
        val dispatchPacket = payload.decodeRawPacket()
        when (dispatchPacket.action) {
            IntentAction.LOGIN -> {
                openMusicPlatformMenu(context.player())
            }

            IntentAction.SHARE -> {
                context.sendMessage(Text.literal("正在准备一些必要的信息用于分享给其他玩家"))
                scope.launch {
                    val songId = dispatchPacket.decode<ShareMusicInbound>().id
                    val songUrl = NCMusic.getSongUrl(songId.toLong())
                    val songDetail = NCMusic.getSongDetail(songId.toLong())
                    ShareMusicOutbound(
                        songDetail.name,
                        songId,
                        songDetail.artists,
                        songUrl,
                        context.player().name.string
                    ).createActionPacket(IntentAction.SHARE).sendToClient(context)
                }
            }

            IntentAction.PLAY -> {
                context.player().playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1f, 1f)
                scope.launch {
                    val songId = dispatchPacket.decode<PlayMusicInbound>().id
                    val songUrl = NCMusic.getSongUrl(songId.toLong())
                    val songDetail = NCMusic.getSongDetail(songId.toLong())
                    context.sendMessage(Text.literal("正在播放: ${songDetail.name}"))
                    val lyric = NCMusic.getLyric(songId.toLong())
                    PlayMusicOutbound(
                        songUrl, songId, songDetail.name,
                        songDetail.artists, songDetail.cover, lyric
                    ).createActionPacket(IntentAction.PLAY).sendToClient(context)
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
                val keyword = dispatchPacket.decode<SearchPayloadInbound>().keyword
                context.sendMessage(Text.literal("正在搜索: ").append(Text.literal(keyword).styled {
                    it.withColor(Formatting.YELLOW)
                }))
                scope.launch {
                    val result = NCMusic.search(keyword)
                    SearchResultOutbound(result.map {
                        SearchResultOutbound.Result(
                            it.id,
                            it.name,
                            it.artists,
                        )
                    }).createActionPacket(IntentAction.SEARCH).sendToClient(context)
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
    }
}