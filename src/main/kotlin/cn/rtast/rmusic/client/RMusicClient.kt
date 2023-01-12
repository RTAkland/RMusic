/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.client

import cn.rtast.rmusic.client.commands.RMusicCommand
import cn.rtast.rmusic.client.network.S2CPacketHandler
import cn.rtast.rmusic.player.MusicPlayer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler

@Environment(EnvType.CLIENT)
object RMusicClient : ClientModInitializer {

    var player: MusicPlayer? = null

    override fun onInitializeClient() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            RMusicCommand().register(dispatcher)
        }
        S2CPacketHandler().register()
        ClientPlayConnectionEvents.DISCONNECT.register { _: ClientPlayNetworkHandler,
                                                         _: MinecraftClient ->
            // 玩家退出游戏或者断开连接自动停止播放
            if (player != null) {
                player?.stop()
                player = null
            }
        }
    }
}