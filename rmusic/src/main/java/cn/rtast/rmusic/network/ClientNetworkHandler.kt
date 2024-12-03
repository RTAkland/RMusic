/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.network

import cn.rtast.rmusic.entity.MusicPayload
import cn.rtast.rmusic.entity.SongInfo
import cn.rtast.rmusic.util.decodeToString
import cn.rtast.rmusic.util.fromJson
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
    ClientPlayNetworking.registerGlobalReceiver(MusicPayload.ID) { payload, context ->
        minecraftClient.player?.playSoundToPlayer(
            SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(),
            SoundCategory.BLOCKS, 1f, 1f
        )
        val packet = payload.payload.decodeToString().fromJson<SongInfo>()
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
}