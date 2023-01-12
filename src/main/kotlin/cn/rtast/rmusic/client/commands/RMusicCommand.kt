/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:33
 */

package cn.rtast.rmusic.client.commands

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.common.command.IRMusicCommand
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.player.MusicPlayer
import cn.rtast.rmusic.utils.Message
import cn.rtast.rmusic.utils.StyleUtil
import com.goxr3plus.streamplayer.enums.Status
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import java.net.URL

class RMusicCommand : IRMusicCommand {

    override fun play(ctx: CommandContext<ServerCommandSource>, id: Int) {
        Message.translatable("rmusic.player.play.wait", ctx)
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        } else {
            RMusicClient.player?.stop()
            Message.translatable("rmusic.player.stop", ctx)
        }
        Thread {
            val res = Music163().getSongUrl(id).data[0]
            RMusicClient.player?.play(URL(res.url))
            Message.translatable("rmusic.player.play", StyleUtil.greenStyle(res.id.toString()), ctx)
        }.start()
    }

    override fun stop(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status != Status.STOPPED) {
            RMusicClient.player?.stop()
            RMusicClient.player = null
            Message.translatable("rmusic.player.stop", ctx)
        } else {
            Message.notPlaying(ctx)
        }
    }

    override fun resume(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status == Status.PAUSED) {
            RMusicClient.player?.resume()
            Message.translatable("rmusic.player.resume", ctx)
        }
    }

    override fun pause(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status != Status.PAUSED) {
            RMusicClient.player?.pause()
            Message.translatable("rmusic.player.pause", ctx)
        } else if (RMusicClient.player?.status == Status.PAUSED) {
            Message.translatable("rmusic.player.pause.off", ctx)
        } else {
            Message.notPlaying(ctx)
        }
    }

    override fun mute(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status == Status.PLAYING) {
            if (RMusicClient.player?.mute == true) {
                RMusicClient.player?.mute = false
                Message.translatable("rmusic.player.mute.off", ctx)
            } else {
                RMusicClient.player?.mute = true
                Message.translatable("rmusic.player.mute.on", ctx)
            }
        } else {
            Message.notPlaying(ctx)
        }
    }

    override fun setVolume(ctx: CommandContext<ServerCommandSource>, value: Double) {
        RMusicClient.player?.setGain(value)
        Message.translatable("rmusic.player.setvolume", (value).toString(), ctx)
    }

    override fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        Message.translatable("rmusic.api.search.netease", StyleUtil.greenStyle(keyword), ctx)
        Thread {
            val result = Music163().search(keyword)
            result.forEach {
                Message.sr(it)
            }
            Message.translatable("rmusic.api.search.tip", result.size.toString(), ctx)
        }.start()
    }

    override fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        Message.translatable("rmusic.session.netease.login.wait", ctx)
        Thread {
            val key = when (Music163().login(email, password)) {
                true -> "rmusic.session.netease.login.success"
                else -> "rmusic.session.netease.login.failure"
            }
            Message.translatable(key, ctx)
        }.start()
    }

    override fun logoutNetease(ctx: CommandContext<ServerCommandSource>) {
        Message.translatable("rmusic.session.netease.logout.wait", ctx)
        val key = when (Music163().logout()) {
            true -> "rmusic.session.netease.logout.success"
            else -> "rmusic.session.netease.logout.failure"
        }
        Message.translatable(key, ctx)
    }
}