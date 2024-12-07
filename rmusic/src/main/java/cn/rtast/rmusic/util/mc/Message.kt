/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.util.mc

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting

private val prefixText
    get() = Text.literal("[RMusic]").styled { style ->
        style.withColor(Formatting.YELLOW)
    }

fun ClientPlayNetworking.Context.sendMessage(text: Text, overlay: Boolean = false) {
    this.player().sendMessage(prefixText.append(text), overlay)
}

fun ServerPlayNetworking.Context.sendMessage(text: Text, overlay: Boolean = false) {
    this.player().sendMessage(prefixText.append(text), overlay)
}

fun ServerPlayerEntity.sendPrefixMessage(text: Text, overlay: Boolean = false) {
    this.sendMessage(prefixText.append(text), overlay)
}
