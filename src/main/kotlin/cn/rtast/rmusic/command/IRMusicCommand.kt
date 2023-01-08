package cn.rtast.rmusic.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType.floatArg
import com.mojang.brigadier.arguments.FloatArgumentType.getFloat
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
        dispatcher.register(
            literal("rmusic").requires { it.hasPermissionLevel(0) }
                .then(
                    literal("play")
                        .then(
                            argument("id", integer())
                                .executes { playMusic(it, getInteger(it, "id"));1 }
                        )
                ).then(
                    literal("stop")
                        .executes { stopMusic(it);1 }
                ).then(
                    literal("resume")
                        .executes { resumeMusic(it);1 }
                ).then(
                    literal("pause")
                        .executes { pauseMusic(it);1 }
                ).then(
                    literal("mute")
                        .executes { muteMusic(it);1 }
                ).then(
                    literal("volume")
                        .then(
                            argument("value", floatArg())
                                .executes { setGain(it, getFloat(it, "value") / 10);1 }
                        )
                ).then(
                    literal("search")
                        .then(
                            argument("keyword", string())
                                .executes { searchNetease(it, getString(it, "keyword"));1 }
                        )
                ).then(
                    literal("login")
                        .then(
                            argument("email", string())
                                .then(
                                    argument("password", string())
                                        .executes {
                                            loginNetease(
                                                it,
                                                getString(it, "email"),
                                                getString(it, "password")
                                            );1
                                        }
                                )
                        )
                ).then(
                    literal("logout")
                        .executes { logoutNetease(it);1 }
                )
        )
    }

    fun playMusic(ctx: CommandContext<ServerCommandSource>, id: Int)
    fun stopMusic(ctx: CommandContext<ServerCommandSource>)
    fun resumeMusic(ctx: CommandContext<ServerCommandSource>)
    fun pauseMusic(ctx: CommandContext<ServerCommandSource>)
    fun muteMusic(ctx: CommandContext<ServerCommandSource>)
    fun setGain(ctx: CommandContext<ServerCommandSource>, value: Float)
    fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String)
    fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String)
    fun logoutNetease(ctx: CommandContext<ServerCommandSource>)
}
