/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.commands

import cn.rtast.rmusic.common.command.IRMusicCommand
import cn.rtast.rmusic.network.S2CPacket
import cn.rtast.rmusic.utils.SearchUtil
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

class RMusicCommand : IRMusicCommand {

    private fun send(cmd: Int, body: String, ctx: CommandContext<ServerCommandSource>) {
        S2CPacket().send("$cmd]$body", ctx.source.player!!)
    }

    override fun play(ctx: CommandContext<ServerCommandSource>, id: Int) {
        send(0, id.toString(), ctx)
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
        send(5, value.toString(), ctx)
    }

    override fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        send(8, "$keyword", ctx)
    }

    override fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        send(6, "$email^$password", ctx)
    }

    override fun logoutNetease(ctx: CommandContext<ServerCommandSource>) {
        send(7, "logout", ctx)
    }
}