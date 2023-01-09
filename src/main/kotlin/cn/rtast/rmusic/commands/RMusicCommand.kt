/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.commands

import cn.rtast.rmusic.command.IRMusicCommand
import cn.rtast.rmusic.network.S2CPacket
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

class RMusicCommand : IRMusicCommand {

    private fun send(msg: String, ctx: CommandContext<ServerCommandSource>) {
        S2CPacket().send(msg, ctx.source.player!!)
    }

    override fun playMusic(ctx: CommandContext<ServerCommandSource>, id: Int) {
        send("play-$id", ctx)
    }

    override fun stopMusic(ctx: CommandContext<ServerCommandSource>) {
        send("stop", ctx)
    }

    override fun resumeMusic(ctx: CommandContext<ServerCommandSource>) {
        send("resume", ctx)
    }

    override fun pauseMusic(ctx: CommandContext<ServerCommandSource>) {
        send("pause", ctx)
    }

    override fun muteMusic(ctx: CommandContext<ServerCommandSource>) {
        send("mute", ctx)
    }

    override fun setGain(ctx: CommandContext<ServerCommandSource>, value: Float) {
        send("volume-$value", ctx)
    }

    override fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        send("search-$keyword", ctx)
    }

    override fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        send("login-$email|$password", ctx)
    }

    override fun logoutNetease(ctx: CommandContext<ServerCommandSource>) {
        send("logout", ctx)
    }
}