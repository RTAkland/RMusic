/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic

import cn.rtast.rmusic.command.RMusicCommand
import cn.rtast.rmusic.entity.payload.RMusicCustomPayload
import cn.rtast.rmusic.util.Http
import cn.rtast.rmusic.util.config.ConfigManager
import cn.rtast.rmusic.util.config.CookieManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RMusicServer : ModInitializer {

    companion object {
        val logger: Logger = LoggerFactory.getLogger("RMusic-main")
        val cookieManager = CookieManager()
        val configManager = ConfigManager()
        var publicIp: String? = null
    }

    override fun onInitialize() {
        PayloadTypeRegistry.playC2S().register(RMusicCustomPayload.ID, RMusicCustomPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(RMusicCustomPayload.ID, RMusicCustomPayload.CODEC)
        CommandRegistrationCallback.EVENT.register(RMusicCommand())
        publicIp = Http.get("https://icanhazip.com/")
        logger.info("RMusic 已加载!")
    }
}