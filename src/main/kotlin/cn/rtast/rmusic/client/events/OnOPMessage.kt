package cn.rtast.rmusic.client.events

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.player.MusicPlayer
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.net.URL

class OnOPMessage {

    private val msg = MinecraftClient.getInstance().player!!

    fun onMessage(buf: PacketByteBuf) {
        val origin = buf.readString().split("]")
        val cmd = origin.first().toInt()
        val body = origin.last()
        when (cmd) {
            0 -> {
                if (RMusicClient.player == null) {
                    RMusicClient.player = MusicPlayer()
                } else {
                    RMusicClient.player?.stop()
                }
                val url = body.split("^").first()
                val id = body.split("^").last()
                Thread {
                    val songName = Music163().songName(id.toInt())
                    RMusicClient.player?.play(URL(url))
                    msg.sendMessage(Text.translatable("rmusic.player.playing", Text.literal(songName)
                        .styled { it.withColor(Formatting.AQUA) })
                        .styled { it.withColor(Formatting.GREEN) })
                }.start()
            }

            1 -> {
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.stop()
                    msg.sendMessage(Text.translatable("rmusic.player.stop")
                        .styled { it.withColor(Formatting.GREEN) })
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            2 -> {
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        RMusicClient.player?.resume()
                    } else {
                        msg.sendMessage(
                            Text.translatable("rmusic.player.resume.not")
                                .styled { it.withColor(Formatting.RED) })
                    }
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            3 -> {
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        msg.sendMessage(Text.translatable("rmusic.player.pause.off")
                            .styled { it.withColor(Formatting.GREEN) })
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

            4 -> {
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

            5 -> {
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.setGain(body.toDouble())
                } else {
                    msg.sendMessage(Text.translatable("rmusic.player.notplaying")
                        .styled { it.withColor(Formatting.RED) }
                    )
                }
            }

            6 -> {
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

            7 -> {
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