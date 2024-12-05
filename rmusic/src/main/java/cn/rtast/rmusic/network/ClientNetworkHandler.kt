/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.network

import cn.rtast.rmusic.entity.payload.ActionPacket
import cn.rtast.rmusic.entity.payload.QRCodeLoginPacket
import cn.rtast.rmusic.entity.payload.RMusicPayload
import cn.rtast.rmusic.entity.payload.ShareMusicPacket
import cn.rtast.rmusic.enums.Action
import cn.rtast.rmusic.util.Renderer
import cn.rtast.rmusic.util.str.decodeToByteArray
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
    ClientPlayNetworking.registerGlobalReceiver(RMusicPayload.ID) { payload, context ->
        minecraftClient.player?.playSoundToPlayer(
            SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(),
            SoundCategory.BLOCKS, 1f, 1f
        )
        val dispatchPacket = payload.payload.fromJson<ActionPacket>()
        when (dispatchPacket.action) {
            Action.LOGIN_QRCODE -> {
                val packet = dispatchPacket.body.decodeToString().fromJson<QRCodeLoginPacket>()
                Renderer.loadQRCode = false
                Renderer.renderQRCode(packet.qrcode.decodeToByteArray())
                try {
                    context.player().sendMessage(
                        Text.translatable("rmusic.after_scan_qrcode")
                            .append(Text.translatable("rmusic.here").styled { style ->
                                style.withColor(Formatting.GREEN).withHoverEvent(
                                    HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        Text.translatable("to_confirm")
                                            .styled { it.withColor(Formatting.GREEN) }
                                    )
                                ).withClickEvent(
                                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm login confirm ${packet.key}")
                                )
                            }), false
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            Action.LOGIN_PHONE_PASSWORD -> TODO()
            Action.LOGIN_EMAIL_PASSWORD -> TODO()
            Action.LOGIN_PHONE_CAPTCHA -> TODO()

            Action.SHARE -> {
                val packet = dispatchPacket.body.decodeToString().fromJson<ShareMusicPacket>()
                val musicText = Text.translatable("rmusic.player_literal").append(Text.literal(packet.fromWho).styled {
                    it.withColor(Formatting.YELLOW)
                }).append(Text.translatable("rmusic.shared_a_song")).append("《${packet.name}》- ${packet.artists}")
                    .styled { it.withColor(Formatting.AQUA) }
                    .append(Text.translatable("rmusic.click")).append(Text.translatable("rmusic.here").styled {
                        it.withColor(Formatting.GREEN)
                            .withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Text.translatable("rmusic.click_to_play", packet.name, packet.artists)
                                )
                            ).withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rm play ${packet.id}"))
                    }).append(Text.translatable("rmusic.to_play_song"))
                context.player()?.sendMessage(musicText, false)
            }
        }
    }
}