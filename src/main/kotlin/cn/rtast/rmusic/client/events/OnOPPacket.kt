package cn.rtast.rmusic.client.events

import cn.rtast.rmusic.api.Music163
import cn.rtast.rmusic.client.RMusicClient
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
                Text.translatable("player.stop").styled { it.withColor(Formatting.GREEN) }, false
            )
        }
    }

    fun onMessage(buf: PacketByteBuf) {
        val origin = buf.readString().split("]")
        val cmd = origin.first().toInt()
        val body = origin.last()
        when (cmd) {
            0 -> {  // 播放音乐
                when (body.split("^").first()) {
                    "163" -> {
                        checkout()
                        val name = body.split("^")[1]
                        val url = body.split("^").last()
                        Thread {
                            RMusicClient.player?.play(URL(url))
                            msg.sendMessage(Text.translatable("player.playing",
                                Text.literal(name).styled { it.withColor(Formatting.AQUA) })
                                .styled { it.withColor(Formatting.GREEN) })
                        }.start()
                    }
                }
            }

            1 -> {  // 停止播放
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.stop()
                    msg.sendMessage(Text.translatable("player.stop").styled { it.withColor(Formatting.GREEN) })
                } else {
                    msg.sendMessage(Text.translatable("player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            2 -> {  // 继续播放
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        RMusicClient.player?.resume()
                        msg.sendMessage(Text.translatable("player.resume")
                            .styled { it.withColor(Formatting.GREEN) })
                    } else {
                        msg.sendMessage(
                            Text.translatable("player.resume.not").styled { it.withColor(Formatting.RED) })
                    }
                } else {
                    msg.sendMessage(Text.translatable("player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            3 -> {  // 暂停播放
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        msg.sendMessage(
                            Text.translatable("player.pause.off").styled { it.withColor(Formatting.GREEN) })
                    } else {
                        RMusicClient.player?.pause()
                        msg.sendMessage(Text.translatable("player.pause")
                            .styled { it.withColor(Formatting.RED) })
                    }
                } else {
                    msg.sendMessage(Text.translatable("player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            4 -> {  // 静音
                val player = RMusicClient.player
                if (player != null) {
                    if (player.mute) {
                        RMusicClient.player?.mute = false
                        msg.sendMessage(Text.translatable("player.mute.off")
                            .styled { it.withColor(Formatting.GREEN) })
                    } else {
                        RMusicClient.player?.mute = true
                        msg.sendMessage(Text.translatable("player.mute.on")
                            .styled { it.withColor(Formatting.GREEN) })
                    }
                } else {
                    msg.sendMessage(Text.translatable("player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            5 -> {  // 设置音量
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.setGain(body.toDouble())
                    msg.sendMessage(Text.translatable("player.setvolume", Text.literal(body)
                        .styled { it.withColor(Formatting.AQUA) })
                        .styled { it.withColor(Formatting.GREEN) })
                } else {
                    msg.sendMessage(Text.translatable("player.notplaying")
                        .styled { it.withColor(Formatting.RED) })
                }
            }

            6 -> {  // 登录
                when (body.split("^").first()) {
                    "163" -> {
                        val cookie = body.split("^")[1]
                        val uid = body.split("^").last().toLong()
                        Music163().writeProfile(cookie, uid)
                    }
                }
            }

            7 -> {  // 登出
                when (body.split("^").first()) {
                    "163" -> {  // extends for future
                        msg.sendMessage(Text.translatable("session.netease.logout.wait")
                            .styled { it.withColor(Formatting.GREEN) })
                        val code = Music163().logout()

                        if (code) {
                            msg.sendMessage(Text.translatable("session.netease.logout.success")
                                .styled { it.withColor(Formatting.GREEN) })
                        } else {
                            msg.sendMessage(Text.translatable("session.netease.logout.success")
                                .styled { it.withColor(Formatting.GREEN) })
                        }
                    }
                }
            }
        }
    }
}