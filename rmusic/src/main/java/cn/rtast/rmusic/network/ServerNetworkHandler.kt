/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/8
 */


package cn.rtast.rmusic.network

import cn.rtast.rmusic.command.RMusicCommand
import cn.rtast.rmusic.entity.payload.RMusicCustomPayload
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.util.mc.decodeRawPacket
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking


fun registerServerReceiver() {
    ServerPlayNetworking.registerGlobalReceiver(RMusicCustomPayload.ID) { payload, context ->
        val dispatchPacket = payload.decodeRawPacket()
        when (dispatchPacket.action) {
            IntentAction.LOGIN -> TODO()
            IntentAction.SHARE -> TODO()
            IntentAction.PLAY -> TODO()
            IntentAction.STOP -> TODO()
            IntentAction.PAUSE -> TODO()
            IntentAction.RESUME -> TODO()
            IntentAction.MUTE -> TODO()
            IntentAction.END_PLAYING -> {
                val playList = RMusicCommand.playerPlayList.getOrElse(context.player()) {
                    mutableListOf()
                }
                if (playList.isNotEmpty()) {
                    val latestMusic = playList.first()
                    playList.removeFirst()
                    RMusicCommand.executePlayMusic(latestMusic.id, latestMusic.platform, context.player())
                }
            }
        }
    }
}