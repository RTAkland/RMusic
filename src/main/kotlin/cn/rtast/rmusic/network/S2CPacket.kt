package cn.rtast.rmusic.network

import cn.rtast.rmusic.RMusic
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity

class S2CPacket {
    fun send(msg: String, player: ServerPlayerEntity) {
        ServerPlayNetworking.send(
            player, RMusic.RMUSIC_PACKET_ID,
            PacketByteBufs.create().writeString(msg)
        )
    }
}