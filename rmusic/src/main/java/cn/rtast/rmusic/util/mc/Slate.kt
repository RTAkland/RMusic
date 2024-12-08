/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/6
 */


package cn.rtast.rmusic.util.mc

import cn.rtast.rmusic.entity.payload.outbound.QRCodeLoginOutbound
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.enums.MusicPlatform
import cn.rtast.rmusic.util.music.KuGouMusic
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.str.encodeToBase64
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.mcbrawls.slate.Slate
import net.mcbrawls.slate.Slate.Companion.slate
import net.mcbrawls.slate.tile.Tile.Companion.tile
import net.minecraft.item.Items
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text

private val scope = CoroutineScope(Dispatchers.IO)

fun openMusicPlatformMenu(context: CommandContext<ServerCommandSource>) {
    val slate = slate {
        title = Text.literal("选择需要登陆的平台")
        tiles {
            this[2, 2] = tile(Items.RED_CONCRETE) {
                tooltip("网易云音乐 -> 二维码")
                onGenericClick { slate, _, _ ->
                    context.source.player!!.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f, 1f)
                    generateQRCode(MusicPlatform.Netease, context, slate)
                }
            }
            this[6, 2] = tile(Items.LIME_CONCRETE) {
                tooltip("酷狗音乐 -> 二维码")
                onGenericClick { slate, _, _ ->
                    context.source.player!!.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f, 1f)
                    generateQRCode(MusicPlatform.KuGou, context, slate)
                }
            }
        }
    }
    slate.open(context.source.player!!)
}

private fun generateQRCode(platform: MusicPlatform, context: CommandContext<ServerCommandSource>, slate: Slate) {
    context.sendFeedback(Text.literal("正在获取二维码..."))
    when (platform) {
        MusicPlatform.Netease -> {
            scope.launch {
                try {
                    val (key, qrcode) = NCMusic.loginByQRCode()
                    QRCodeLoginOutbound(key, qrcode.encodeToBase64(), MusicPlatform.Netease)
                        .createActionPacket(IntentAction.LOGIN)
                        .sendToClient(context)
                } catch (e: Exception) {
                    e.printStackTrace()
                    context.sendFeedback(Text.literal("获取二维码失败: ${e.message}"))
                }
            }
        }

        MusicPlatform.KuGou -> {
            scope.launch {
                try {
                    val (key, qrcode) = KuGouMusic.loginByQRCode()
                    QRCodeLoginOutbound(key, qrcode.encodeToBase64(), MusicPlatform.KuGou)
                        .createActionPacket(IntentAction.LOGIN)
                        .sendToClient(context)
                } catch (e: Exception) {
                    e.printStackTrace()
                    context.sendFeedback(Text.literal("获取二维码失败: ${e.message}"))
                }
            }
        }

        MusicPlatform.QQ -> TODO()
    }
    slate.close(context.source.player!!)
}