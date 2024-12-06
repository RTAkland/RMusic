/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.entity.payload.ActionPacket
import cn.rtast.rmusic.entity.payload.RMusicCustomPayload
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.util.str.decodeToString
import cn.rtast.rmusic.util.str.encodeToBase64
import cn.rtast.rmusic.util.str.fromJson
import cn.rtast.rmusic.util.str.toJson
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity

fun Any.createActionPacket(action: IntentAction): ActionPacket {
    return ActionPacket(action, this.toJson().encodeToBase64())
}

fun ActionPacket.sendToServer() {
    ClientPlayNetworking.send(RMusicCustomPayload(this.toJson()))
}

fun ActionPacket.sendToClient(context: ServerPlayNetworking.Context) {
    ServerPlayNetworking.send(context.player(), RMusicCustomPayload(this.toJson()))
}

fun ActionPacket.sendToClient(player: ServerPlayerEntity) {
    ServerPlayNetworking.send(player, RMusicCustomPayload(this.toJson()))
}

fun RMusicCustomPayload.decodeRawPacket(): ActionPacket {
    return this.payload.fromJson<ActionPacket>()
}

inline fun <reified T> ActionPacket.decode(): T {
    return this.body.decodeToString().fromJson<T>()
}

