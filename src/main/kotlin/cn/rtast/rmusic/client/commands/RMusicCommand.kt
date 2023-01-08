package cn.rtast.rmusic.client.commands

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.command.IRMusicCommand
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.player.CommonPlayer
import cn.rtast.rmusic.utils.Message
import cn.rtast.rmusic.utils.SearchUtil
import cn.rtast.rmusic.utils.StyleUtil
import com.goxr3plus.streamplayer.enums.Status
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

class RMusicCommand : IRMusicCommand {

    override fun playMusic(ctx: CommandContext<ServerCommandSource>, id: Int) {
        if (RMusicClient.player == null) {
            RMusicClient.player = CommonPlayer()
            Thread {
                RMusicClient.player?.playMusic(id)
                Message.translatable("rmusic.player.play", StyleUtil.greenStyle(id.toString()), ctx)
            }.start()
        } else {
            RMusicClient.player!!.stop()
            Message.translatable("rmusic.player.stop", ctx)
            Thread {
                RMusicClient.player!!.playMusic(id)
                Message.translatable("rmusic.player.play", StyleUtil.greenStyle(id.toString()), ctx)
            }.start()
        }
        Message.translatable("rmusic.player.play.wait", ctx)
    }

    override fun stopMusic(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status != Status.STOPPED) {
            RMusicClient.player?.stop()
            RMusicClient.player = null
            Message.translatable("rmusic.player.stop", ctx)
        } else {
            Message.notPlaying(ctx)
        }
    }

    override fun resumeMusic(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status == Status.PAUSED) {
            RMusicClient.player?.resume()
            Message.translatable("rmusic.player.resume", ctx)
        }
    }

    override fun pauseMusic(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status == Status.PAUSED) {
            RMusicClient.player?.pause()
            Message.translatable("rmusic.player.pause", ctx)
        } else {
            Message.notPlaying(ctx)
        }
    }

    override fun muteMusic(ctx: CommandContext<ServerCommandSource>) {
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

    override fun setGain(ctx: CommandContext<ServerCommandSource>, value: Float) {
        RMusicClient.player?.setGain(value.toDouble())
        Message.translatable("rmusic.player.setvolume", (value * 10).toString(), ctx)
    }

    override fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        Message.translatable("rmusic.api.search.netease", keyword, ctx)
        Thread {
            val result = SearchUtil().searchNetease(keyword)
            result.forEach {
                Message.sr(it, ctx)
            }
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