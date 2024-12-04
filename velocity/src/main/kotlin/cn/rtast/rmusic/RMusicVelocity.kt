/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */

@file:Suppress("unused")

package cn.rtast.rmusic

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PluginMessageEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import org.slf4j.Logger

class RMusicVelocity @Inject constructor(
    private val logger: Logger,
    private val proxyServer: ProxyServer
) {
    private val channelId = MinecraftChannelIdentifier.from("rmusic:networking")

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        proxyServer.channelRegistrar.register(channelId)
        logger.info("RMusic已加载")
    }

    @Subscribe
    fun onPluginMessageFromPlayer(event: PluginMessageEvent) {
        proxyServer.allPlayers.forEach {
            it.sendPluginMessage(channelId, event.data)
        }
    }
}