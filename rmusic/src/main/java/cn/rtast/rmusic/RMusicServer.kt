/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic

import cn.rtast.rmusic.entity.payload.RMusicCustomPayload
import cn.rtast.rmusic.network.registerServerReceiver
import cn.rtast.rmusic.util.CookieManager
import cn.rtast.rmusic.util.config.ServerConfigManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RMusicServer : ModInitializer {

    companion object {
        val logger: Logger = LoggerFactory.getLogger("RMusic-main")
        val loginManager = CookieManager()
        val configManager = ServerConfigManager()
    }

    override fun onInitialize() {
        PayloadTypeRegistry.playC2S().register(RMusicCustomPayload.ID, RMusicCustomPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(RMusicCustomPayload.ID, RMusicCustomPayload.CODEC)
        registerServerReceiver()
        logger.info("RMusic 已加载!")
    }
}