package cn.rtast.rmusic.client.events

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.player.MusicPlayer
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.net.URL

class OnOPPacket {

    private val msg = MinecraftClient.getInstance().player!!

    private fun checkout() {
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        } else {
            RMusicClient.player?.stop()
            msg.sendMessage(
                Text.translatable("rmusic.player.stop").styled { it.withColor(Formatting.GREEN) }, false
            )
        }
    }

    fun onMessage(buf: PacketByteBuf) {
        val origin = buf.readString().split("]")
        val cmd = origin.first().toInt()
        val body = origin.last()
        when (cmd) {
            0 -> {  // 播放音乐
                msg.sendMessage(
                    Text.translatable("rmusic.player.play.waiting").styled { it.withColor(Formatting.GREEN) }, false
                )
                checkout()
                Thread {
                    val info = Music163().getSongUrl(body.toInt()).data[0]
                    val songName = Music163().songName(body.toInt())
                    RMusicClient.player?.play(URL(info.url))
                    msg.sendMessage(Text.translatable("rmusic.player.playing",
                        Text.literal(songName).styled { it.withColor(Formatting.AQUA) })
                        .styled { it.withColor(Formatting.GREEN) })
                }.start()
            }

            1 -> {  // 停止播放
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.stop()
                    msg.sendMessage(Text.translatable("rmusic.player.stop").styled { it.withColor(Formatting.GREEN) })
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            2 -> {  // 继续播放
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        RMusicClient.player?.resume()
                        msg.sendMessage(Text.translatable("rmusic.player.resume")
                            .styled { it.withColor(Formatting.GREEN) })
                    } else {
                        msg.sendMessage(
                            Text.translatable("rmusic.player.resume.not").styled { it.withColor(Formatting.RED) })
                    }
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            3 -> {  // 暂停播放
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        msg.sendMessage(
                            Text.translatable("rmusic.player.pause.off").styled { it.withColor(Formatting.GREEN) })
                    } else {
                        RMusicClient.player?.pause()
                        msg.sendMessage(Text.translatable("rmusic.player.pause")
                            .styled { it.withColor(Formatting.RED) })
                    }
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            4 -> {  // 静音
                val player = RMusicClient.player
                if (player != null) {
                    if (player.mute) {
                        RMusicClient.player?.mute = false
                        msg.sendMessage(Text.translatable("rmusic.player.mute.off")
                            .styled { it.withColor(Formatting.GREEN) })
                    } else {
                        RMusicClient.player?.mute = true
                        msg.sendMessage(Text.translatable("rmusic.player.mute.on")
                            .styled { it.withColor(Formatting.GREEN) })
                    }
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            5 -> {  // 设置音量
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.setGain(body.toDouble())
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            6 -> {  // 登录
                val email = body.split("^").first()
                val password = body.split("^").last()
                msg.sendMessage(Text.translatable("rmusic.session.netease.login.wait")
                    .styled { it.withColor(Formatting.GREEN) })
                Thread {  // 耗时操作单独启动线程
                    val code = Music163().login(email, password)
                    if (code) {
                        msg.sendMessage(Text.translatable("rmusic.session.netease.login.success")
                            .styled { it.withColor(Formatting.GREEN) })
                    } else {
                        msg.sendMessage(Text.translatable("rmusic.session.netease.login.failure")
                            .styled { it.withColor(Formatting.RED) })
                    }
                }.start()
            }

            7 -> {  // 登出
                msg.sendMessage(Text.translatable("rmusic.session.netease.logout.wait")
                    .styled { it.withColor(Formatting.GREEN) })
                val code = Music163().logout()
                if (code) {
                    msg.sendMessage(Text.translatable("rmusic.session.netease.logout.success")
                        .styled { it.withColor(Formatting.GREEN) })
                } else {
                    msg.sendMessage(Text.translatable("rmusic.session.netease.logout.success")
                        .styled { it.withColor(Formatting.GREEN) })
                }
            }
        }
    }
}