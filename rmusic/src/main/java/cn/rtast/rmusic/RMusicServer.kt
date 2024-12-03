/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic

import cn.rtast.rmusic.RMusicClient.Companion.logger
import cn.rtast.rmusic.entity.MusicPayload
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

class RMusicServer: ModInitializer {
    override fun onInitialize() {
        PayloadTypeRegistry.playC2S().register(MusicPayload.ID, MusicPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(MusicPayload.ID, MusicPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(MusicPayload.ID) { payload, context ->
            context.server().playerManager.playerList.forEach {
                ServerPlayNetworking.send(it, payload)
            }
        }
        logger.info("RMusic 已加载!")
    }
}