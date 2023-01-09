package cn.rtast.rmusic.utils

import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class Message {
    fun m(ctx: CommandContext<ServerCommandSource>, msg: String) {
        ctx.source.sendFeedback(Text.literal(msg), false)
    }

    fun notPlayed(ctx: CommandContext<ServerCommandSource>) {
        ctx.source.sendFeedback(Text.literal("当前还未播放音乐!"), false)
    }

    fun notClient(ctx: CommandContext<ServerCommandSource>) {
        ctx.source.sendFeedback(Text.literal("请在客户端执行命令!"), false)
    }
}