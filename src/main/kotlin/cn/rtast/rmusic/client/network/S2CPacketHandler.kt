/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:29
 */

package cn.rtast.rmusic.client.network

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.client.events.OnOPMessage
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

class S2CPacketHandler {
    // 接收服务端发送的数据包
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(RMusic.RMUSIC_PACKET_ID)
        { _: MinecraftClient,
          _: ClientPlayNetworkHandler,
          buf: PacketByteBuf,
          _: PacketSender ->
            OnOPMessage().onMessage(buf)
        }
    }
}