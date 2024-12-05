/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.util

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text

fun ClientPlayNetworking.Context.sendMessage(text: Text, overlay: Boolean = false) {
    this.player().sendMessage(text, overlay)
}

fun ServerPlayNetworking.Context.sendMessage(text: Text, overlay: Boolean = false) {
    this.player().sendMessage(text, overlay)
}