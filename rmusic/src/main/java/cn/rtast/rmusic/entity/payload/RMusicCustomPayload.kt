/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.payload

import cn.rtast.rmusic.networkingId
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

@JvmRecord
data class RMusicCustomPayload(val payload: String) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }

    companion object {
        val ID = CustomPayload.Id<RMusicCustomPayload>(networkingId)
        val CODEC: PacketCodec<PacketByteBuf, RMusicCustomPayload> = PacketCodec.of({ value, buf ->
            buf.writeString(value.payload)
        }, { buf -> return@of RMusicCustomPayload(buf.readString()) })
    }
}