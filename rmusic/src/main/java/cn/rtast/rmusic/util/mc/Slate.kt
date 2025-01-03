/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/6
 */


package cn.rtast.rmusic.util.mc

import cn.rtast.rmusic.entity.payload.outbound.QRCodeLoginOutbound
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.enums.MusicPlatform
import cn.rtast.rmusic.enums.PlayerHead
import cn.rtast.rmusic.enums.PlayerHead.Companion.forItem
import cn.rtast.rmusic.scope
import cn.rtast.rmusic.util.music.KuGouMusic
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.str.encodeToBase64
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.launch
import net.mcbrawls.slate.Slate
import net.mcbrawls.slate.Slate.Companion.slate
import net.mcbrawls.slate.tile.Tile.Companion.tile
import net.mcbrawls.slate.tile.TileGrid
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

fun openMusicPlatformMenu(context: CommandContext<ServerCommandSource>) {
    val slate = slate {
        tiles = TileGrid.create(ScreenHandlerType.GENERIC_9X1)
        title = Text.literal("选择需要登陆的平台")
        tiles {
            this[0, 0] = tile(PlayerHead.W.forItem()) {
                tooltip("网易云音乐 -> 二维码")
                onGenericClick { slate, _, slateContext ->
                    slateContext.player.playButtonSound()
                    generateQRCode(MusicPlatform.Netease, context, slate)
                }
            }
            this[1, 0] = tile(PlayerHead.Q.forItem()) {
                tooltip("QQ音乐")
                onGenericClick { slate, _, slateContext ->
                    slateContext.player.playButtonSound()
                    context.sendFeedback(Text.literal("暂不支持QQ音乐登录"))
                    slate.close(context.source.player!!)
                }
            }
            this[2, 0] = tile(PlayerHead.K.forItem()) {
                tooltip("酷狗音乐 -> 二维码")
                onGenericClick { slate, _, slateContext ->
                    slateContext.player.playButtonSound()
                    generateQRCode(MusicPlatform.KuGou, context, slate)
                }
            }
            this[8, 0] = tile(Items.BARRIER) {
                tooltip("关闭菜单")
                onGenericClick { slate, _, slateContext ->
                    slateContext.player.playButtonSound()
                    slate.close(context.source.player!!)
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