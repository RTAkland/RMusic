package cn.rtast.rmusic.client.events

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.player.CommonPlayer
import cn.rtast.rmusic.utils.Message
import cn.rtast.rmusic.utils.SearchUtil
import cn.rtast.rmusic.utils.SessionStateUtil
import cn.rtast.rmusic.utils.StyleUtil
import com.goxr3plus.streamplayer.enums.Status
import net.minecraft.network.PacketByteBuf

class OnOPMessage {

    fun onMessage(buf: PacketByteBuf) {
        val message = buf.readString()
        if (message.startsWith("play")) {
            val id = message.split("-").last().toInt()
            if (RMusicClient.player == null) {
                RMusicClient.player = CommonPlayer()
                Thread {
                    RMusicClient.player?.playMusic(id)
                    Message.translatable("rmusic.player.play", StyleUtil.greenStyle(id.toString()))
                }.start()
            } else {
                RMusicClient.player!!.stop()
                Message.translatable("rmusic.player.stop")
                Thread {
                    RMusicClient.player!!.playMusic(id)
                    Message.translatable("rmusic.player.play", StyleUtil.greenStyle(id.toString()))
                }.start()
            }
            Message.translatable("rmusic.player.play.wait")
        } else if (message.startsWith("stop")) {
            if (RMusicClient.player?.status != Status.STOPPED) {
                RMusicClient.player?.stop()
            } else {
                Message.notPlaying()
            }
        } else if (message.startsWith("pause")) {
            if (RMusicClient.player != null) {
                if (RMusicClient.player?.status != Status.PAUSED) {
                    RMusicClient.player?.pause()
                }
            } else {
                Message.notPlaying()
            }
        } else if (message.startsWith("resume")) {
            if (RMusicClient.player != null) {
                if (RMusicClient.player?.status == Status.PAUSED) {
                    RMusicClient.player?.resume()
                }
            } else {
                Message.notPlaying()
            }
        } else if (message.startsWith("login")) {
            Message.translatable("rmusic.session.netease.login.wait")
            Thread {
                val email = message.split("-").last().split("|").first()
                val password = message.split("-").last().split("|").last()
                val state = Music163().login(email, password)
                if (state) {
                    Message.translatable("rmusic.session.netease.login.success")
                } else {
                    Message.translatable("rmusic.session.netease.login.failure")
                }
            }.start()
        } else if (message.startsWith("logout")) {
            Message.translatable("rmusic.session.netease.logout.wait")
            if (SessionStateUtil.netease()) {
                Music163().logout()
                Message.translatable("rmusic.session.netease.login.success")
            } else {
                Message.translatable("rmusic.session.netease.logout.failure")
            }
        } else if (message.startsWith("search")) {
            val keyword = message.split("-").last()
            Message.translatable("rmusic.api.search.netease", keyword)
            Thread {
                val result = SearchUtil().searchNetease(keyword)
                result.forEach {
                    Message.sr(it)
                }
            }.start()

        } else if (message.startsWith("volume")) {
            if (RMusicClient.player != null) {
                val volume = message.split("-").last().toDouble()
                RMusicClient.player?.setGain(volume)
                Message.translatable("rmusic.player.setvolume", (volume * 10).toString())
            }
        } else if (message.startsWith("mute")) {
            if (RMusicClient.player != null) {
                if (RMusicClient.player?.mute == true) {
                    RMusicClient.player?.mute = false
                    Message.translatable("rmusic.player.mute.off")
                } else {
                    RMusicClient.player?.mute = true
                    Message.translatable("rmusic.player.mute.on")
                }
            }
        }
    }
}