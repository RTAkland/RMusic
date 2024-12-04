/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/3
 */


package cn.rtast.rmusic.entity.payload

import cn.rtast.rmusic.actionNetworkingId
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

@JvmRecord
data class ShareMusicCustomPayload(val payload: String) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }

    companion object {
        val ID = CustomPayload.Id<ShareMusicCustomPayload>(actionNetworkingId)
        val CODEC: PacketCodec<PacketByteBuf, ShareMusicCustomPayload> = PacketCodec.of({ value, buf ->
            buf.writeString(value.payload)
        }, { buf -> return@of ShareMusicCustomPayload(buf.readString()) })
    }
}