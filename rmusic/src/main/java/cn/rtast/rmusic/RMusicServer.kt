/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic

import cn.rtast.rmusic.command.ServerRMusicCommand
import cn.rtast.rmusic.entity.payload.RMusicPayload
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

class RMusicServer : ModInitializer {
    private val logger = LoggerFactory.getLogger("RMusic-server")
    override fun onInitialize() {
        PayloadTypeRegistry.playC2S().register(RMusicPayload.ID, RMusicPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(RMusicPayload.ID, RMusicPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(RMusicPayload.ID) { payload, context ->
            context.server().playerManager.playerList.forEach {
                ServerPlayNetworking.send(it, payload)
            }
        }
        CommandRegistrationCallback.EVENT.register(ServerRMusicCommand())//
        logger.info(Text.translatable("rmusic.loaded").string)
    }
}