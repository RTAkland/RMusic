/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.network

import cn.rtast.rmusic.RMusic
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity

class S2CPacket {
    // 向客户端发送数据包
    fun send(msg: String, player: ServerPlayerEntity) {
        ServerPlayNetworking.send(
            player, RMusic.RMUSIC_PACKET_ID,
            PacketByteBufs.create().writeString(msg)
        )
    }
}