/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.commands

import cn.rtast.rmusic.network.S2CPacket
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg
import com.mojang.brigadier.arguments.DoubleArgumentType.getDouble
import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

open class RMusicCommand{
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val literalBuilder = literal("rmusic")
            .requires { it.hasPermissionLevel(0) }
            .then(
                literal("play")
                    .then(
                        argument("id", integer())
                            .executes { play(it, getInteger(it, "id"));1 }
                    )
            )
            .then(
                literal("pause")
                    .executes { pause(it);1 }
            )
            .then(
                literal("resume")
                    .executes { resume(it);1 }
            )
            .then(
                literal("mute")
                    .executes { mute(it);1 }
            )
            .then(
                literal("volume")
                    .then(
                        argument("volume", doubleArg())
                            .executes { setVolume(it, getDouble(it, "volume"));1 }
                    )
            )
            .then(
                literal("stop")
                    .executes { stop(it);1 }
            )
            .then(
                literal("search")
                    .then(
                        argument("kwd", string())
                            .executes { search163(it, getString(it, "kwd"));1 }
                    )
            )
            .then(
                literal("login")
                    .then(
                        literal("163")
                            .then(
                                argument("email", string())
                                    .then(
                                        argument("pwd", string())
                                            .executes {
                                                login163(it, getString(it, "email"), getString(it, "pwd"));1
                                            }
                                    )
                            )
                    )
            )
            .then(
                literal("logout")
                    .executes { logout163(it);1 }
            )
        dispatcher.register(literalBuilder)
    }

    private fun send(cmd: Int, body: String, ctx: CommandContext<ServerCommandSource>) {
        S2CPacket().send("$cmd]$body", ctx.source.player!!)
    }

    open fun play(ctx: CommandContext<ServerCommandSource>, id: Int) {
        send(0, "163^$id", ctx)
    }

    open fun pause(ctx: CommandContext<ServerCommandSource>) {
        send(3, "pause", ctx)
    }

    open fun resume(ctx: CommandContext<ServerCommandSource>) {
        send(2, "resume", ctx)
    }

    open fun mute(ctx: CommandContext<ServerCommandSource>) {
        send(4, "mute", ctx)
    }

    open fun setVolume(ctx: CommandContext<ServerCommandSource>, value: Double) {
        send(5, value.toString(), ctx)
    }

    open fun stop(ctx: CommandContext<ServerCommandSource>) {
        send(1, "stop", ctx)
    }

    open fun search163(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        send(8, "163^$keyword", ctx)
    }

    open fun login163(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        send(6, "163^$email^$password", ctx)
    }

    open fun logout163(ctx: CommandContext<ServerCommandSource>) {
        send(7, "163^logout", ctx)
    }
}