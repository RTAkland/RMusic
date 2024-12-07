/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.util.mc

import cn.rtast.rmusic.entity.payload.ActionPacket
import cn.rtast.rmusic.entity.payload.RMusicCustomPayload
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.util.str.decodeToString
import cn.rtast.rmusic.util.str.encodeToBase64
import cn.rtast.rmusic.util.str.fromJson
import cn.rtast.rmusic.util.str.toJson
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity

fun Any.createActionPacket(action: IntentAction): ActionPacket {
    return ActionPacket(action, this.toJson().encodeToBase64())
}

fun ActionPacket.sendToServer(): Int {
    ClientPlayNetworking.send(RMusicCustomPayload(this.toJson()))
    return 0
}

fun ActionPacket.sendToClient(context: CommandContext<ServerCommandSource>): Int {
    ServerPlayNetworking.send(context.source.player, RMusicCustomPayload(this.toJson()))
    return 0
}

fun ActionPacket.sendToClient(player: ServerPlayerEntity): Int {
    ServerPlayNetworking.send(player, RMusicCustomPayload(this.toJson()))
    return 0
}

fun RMusicCustomPayload.decodeRawPacket(): ActionPacket {
    return this.payload.fromJson<ActionPacket>()
}

inline fun <reified T> ActionPacket.decode(): T {
    return this.body.decodeToString().fromJson<T>()
}

