package cn.rtast.rmusic.client.events

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.player.MusicPlayer
import cn.rtast.rmusic.utils.Message
import cn.rtast.rmusic.utils.StyleUtil
import net.minecraft.network.PacketByteBuf
import java.net.URL

class OnOPMessage {

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
                RMusicClient.player?.play(URL(url))
                Message.translatable("rmusic.player.playing", id)
            }

            1 -> {
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.stop()
                } else {
                    Message.notPlaying()
                }
            }

            2 -> {
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        RMusicClient.player?.resume()
                    } else {
                        Message.translatable(
                            "rmusic.player.resume.not", StyleUtil.redStyle("rmusic.player.resume.not")
                        )
                    }
                } else {
                    Message.notPlaying()
                }
            }

            3 -> {
                val player = RMusicClient.player
                if (player != null) {
                    if (player.isPaused) {
                        RMusicClient.player?.resume() // pause 命令可以暂停也可以继续播放
                    } else {
                        RMusicClient.player?.pause()
                    }
                } else {
                    Message.notPlaying()
                }
            }

            4 -> {
                val player = RMusicClient.player
                if (player != null) {
                    if (player.mute) {
                        RMusicClient.player?.mute = false
                        Message.translatable("rmusic.player.mute.off")
                    } else {
                        RMusicClient.player?.mute = true
                        Message.translatable("rmusic.player.mute.on")
                    }
                } else {
                    Message.notPlaying()
                }
            }

            5 -> {
                Message.translatable("rmusic.api.search.netease", body)
                Thread {
                    val result = Music163().search(body)
                    result.forEach {
                        Message.sr(it)
                    }
                    Message.translatable("rmusic.api.search.tip", result.size.toString())
                }.start()
            }

            6 -> {
                val player = RMusicClient.player
                if (player != null) {
                    RMusicClient.player?.setGain(body.toDouble())
                } else {
                    Message.notPlaying()
                }
            }

            7 -> {
                val email = body.split("^").first()
                val password = body.split("^").last()
                Message.translatable("rmusic.session.netease.login.wait")
                Thread {  // 耗时操作单独启动线程
                    val code = Music163().login(email, password)
                    if (code) {
                        Message.translatable("rmusic.session.netease.login.success")
                    } else {
                        Message.translatable("rmusic.session.netease.login.failure")
                    }
                }.start()
            }

            8 -> {
                Message.translatable("rmusic.session.netease.logout.wait")
                val code = Music163().logout()
                if (code) {
                    Message.translatable("rmusic.session.netease.logout.success")
                } else {
                    Message.translatable("rmusic.session.netease.logout.failure")
                }
            }
        }
    }
}