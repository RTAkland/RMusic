/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.commands

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.api.Music163
import cn.rtast.rmusic.network.S2CPacket
import cn.rtast.rmusic.utils.ConfigUtil
import cn.rtast.rmusic.utils.SearchUtil
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
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class RMusicCommand {

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val rmusicNode = dispatcher.register(literal("rmusic")
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
                            .executes { setVolume(it, getDouble(it, "volume") / 10);1 }
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
            .then(
                literal("set-url")
                    .requires { it.hasPermissionLevel(3) }
                    .then(
                        argument("url", string())
                            .executes { setUrl(it, getString(it, "url"));1 }
                    )
            )
            .then(
                literal("reload")
                    .requires { it.hasPermissionLevel(3) }
                    .executes { reload(it);1 }
            )
        )
        dispatcher.register(literal("rm").redirect(rmusicNode))
    }

    private fun send(cmd: Int, body: String, ctx: CommandContext<ServerCommandSource>) {
        S2CPacket().send("$cmd]$body", ctx.source.player!!)
    }

    open fun play(ctx: CommandContext<ServerCommandSource>, id: Int) {
        ctx.source.sendFeedback(
            Text.translatable("player.play.waiting").styled { it.withColor(Formatting.GREEN) },
            false
        )
        Thread {
            val info = Music163().getSongUrl(id)
            send(0, "163^${info.songName}^${info.url}", ctx)
        }.start()
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
        SearchUtil("163").search(ctx, keyword)
    }

    open fun login163(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        if (ctx.source.isExecutedByPlayer) {
            ctx.source.sendFeedback(
                Text.translatable("session.netease.login.wait")
                    .styled { it.withColor(Formatting.GREEN) }, false
            )
            Thread {
                val result = Music163().getCookie(email, password)
                if (result != null) {
                    ctx.source.sendFeedback(
                        Text.translatable("session.netease.login.success")
                            .styled { it.withColor(Formatting.GREEN) }, false
                    )
                    send(6, "163^$result", ctx)
                } else {
                    ctx.source.sendFeedback(
                        Text.translatable("session.netease.login.failure")
                            .styled { it.withColor(Formatting.RED) }, false
                    )
                }
            }.start()
        } else {
            println("[RMusic]?????????????????????????????????cookie.json?????????????????????, ???????????????????????????cookie.")
            println("[RMusic]???????????????...")
            Thread {
                if (Music163().login(email, password)) {
                    println("[RMusic]????????????, cookie.json???????????????./config/rmusic/cookie.json???.")
                } else {
                    println("[RMusic]????????????, ????????????????????????????????????!")
                }
            }.start()
        }
    }

    open fun logout163(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.isExecutedByPlayer) {
            send(7, "163^logout", ctx)
        } else {
            println("[RMusic]????????????????????????, ???????????????????????????cookie.json??????, ?????????????????????.")
            println("[RMusic]????????????...")
            if (Music163().logout()) {
                println("[RMusic]????????????, ?????????cookie.json")
            } else {
                println("[RMusic]????????????!")
            }
        }
    }

    open fun setUrl(ctx: CommandContext<ServerCommandSource>, url: String) {
        // ???????????????????????????api??????, ????????????????????????
        ctx.source.sendFeedback(
            Text.translatable("config.modify.wait")
                .styled { it.withColor(Formatting.GREEN) }, false
        )
        Thread {
            try {
                ConfigUtil().set163URL(url)
                RMusic.MUSIC_API_163 = url
                ctx.source.sendFeedback(
                    Text.translatable("config.modify.new", Text.literal(url)
                        .styled { it.withColor(Formatting.AQUA) })
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            } catch (_: Exception) {
                ctx.source.sendFeedback(
                    Text.translatable("config.modify.failure")
                        .styled { it.withColor(Formatting.RED) }, false
                )
            }
        }.start()
    }

    open fun reload(ctx: CommandContext<ServerCommandSource>) {
        // ????????????????????????????????????????????????
        ctx.source.sendFeedback(
            Text.translatable("config.reload.wait")
                .styled { it.withColor(Formatting.GREEN) }, false
        )
        try {
            val url = ConfigUtil().get163URL()
            RMusic.MUSIC_API_163 = url
            ctx.source.sendFeedback(
                Text.translatable("config.reload.success")
                    .styled { it.withColor(Formatting.YELLOW) }, false
            )
        } catch (_: Exception) {
            ctx.source.sendFeedback(
                Text.translatable("config.reload.failure")
                    .styled { it.withColor(Formatting.RED) }, false
            )
        }
    }
}