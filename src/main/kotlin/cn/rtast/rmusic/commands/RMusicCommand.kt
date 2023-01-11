/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.commands

import cn.rtast.rmusic.common.command.IRMusicCommand
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.network.S2CPacket
import cn.rtast.rmusic.utils.Message
import cn.rtast.rmusic.utils.StyleUtil
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

class RMusicCommand : IRMusicCommand {

    private fun send(cmd: Int, body: String, ctx: CommandContext<ServerCommandSource>) {
        S2CPacket().send("$cmd]$body", ctx.source.player!!)
    }

    override fun play(ctx: CommandContext<ServerCommandSource>, id: Int) {
        Message.translatable("rmusic.player.play.waiting", ctx)
        Thread {
            try {
                val info = Music163().getSongUrl(id)
                send(0, "${info.data[0].url}^${info.data[0].id}", ctx)
            } catch (_: IndexOutOfBoundsException) {
                Message.translatable(
                    "rmusic.player.play.get.failure", StyleUtil.redStyle("rmusic.player.play.get"), ctx
                )
            }
        }.start()
    }

    override fun stop(ctx: CommandContext<ServerCommandSource>) {
        send(1, "stop", ctx)
    }

    override fun resume(ctx: CommandContext<ServerCommandSource>) {
        send(2, "resume", ctx)
    }

    override fun pause(ctx: CommandContext<ServerCommandSource>) {
        send(3, "pause", ctx)
    }

    override fun mute(ctx: CommandContext<ServerCommandSource>) {
        send(4, "mute", ctx)
    }

    override fun setVolume(ctx: CommandContext<ServerCommandSource>, value: Double) {
        send(5, "$value", ctx)
    }

    override fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        Message.translatable("rmusic.api.search.netease", keyword, ctx)
        Thread {
            val result = Music163().search(keyword)
            result.forEach {
                Message.sr(it, ctx)
            }
            Message.translatable("rmusic.api.search.tip", result.size.toString(), ctx)
        }.start()
    }

    override fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        send(6, "$email^$password", ctx)
    }

    override fun logoutNetease(ctx: CommandContext<ServerCommandSource>) {
        send(7, "logout", ctx)
    }
}