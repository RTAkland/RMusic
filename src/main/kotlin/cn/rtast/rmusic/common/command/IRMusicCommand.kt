/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.common.command

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

interface IRMusicCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal("rmusic").requires { it.hasPermissionLevel(0) }
            .then(
                literal("play")
                    .then(
                        argument("id", integer())
                            .executes { play(it, getInteger(it, "id"));1 }
                    )
            )
            .then(
                literal("stop")
                    .executes { stop(it);1 }).then(literal("resume").executes { resume(it);1 }
            )
            .then(
                literal("pause")
                    .executes { pause(it);1 }).then(literal("mute").executes { mute(it);1 }
            )
            .then(
                literal("volume")
                    .then(argument("value", doubleArg())
                        .executes { setVolume(it, getDouble(it, "value") / 10);1 }
                    )
            )
            .then(
                literal("search")
                    .then(argument("keyword", string())
                        .executes { searchNetease(it, getString(it, "keyword"));1 }
                    )
            )
            .then(
                literal("login")
                    .then(argument("email", string())
                        .then(argument("password", string())
                            .executes { loginNetease(it, getString(it, "email"), getString(it, "password"));1 }
                        )
                    )
            )
            .then(
                literal("logout")
                    .executes { logoutNetease(it);1 }
            )
        )
    }

    fun play(ctx: CommandContext<ServerCommandSource>, id: Int)

    fun stop(ctx: CommandContext<ServerCommandSource>)

    fun resume(ctx: CommandContext<ServerCommandSource>)

    fun pause(ctx: CommandContext<ServerCommandSource>)

    fun mute(ctx: CommandContext<ServerCommandSource>)

    fun setVolume(ctx: CommandContext<ServerCommandSource>, value: Double)

    fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String)

    fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String)

    fun logoutNetease(ctx: CommandContext<ServerCommandSource>)
}
