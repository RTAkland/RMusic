/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic

import cn.rtast.rmusic.entity.MusicPayload
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import org.slf4j.LoggerFactory

class RMusicServer: ModInitializer {
    private val logger = LoggerFactory.getLogger("RMusic-server")
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