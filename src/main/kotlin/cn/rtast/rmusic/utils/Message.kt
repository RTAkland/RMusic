/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.utils

import com.mojang.brigadier.context.CommandContext
import net.minecraft.client.MinecraftClient
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.MutableText
import net.minecraft.text.Text

object Message {

    fun actionBar(msg: String) {
        TODO("send messages to action bar")
    }

    fun sr(msg: String) {
        MinecraftClient.getInstance().player?.sendMessage(StyleUtil.resultStyle(msg))
    }

    fun sr(msg: String, s: CommandContext<ServerCommandSource>) {
        s.source.sendFeedback(StyleUtil.resultStyle(msg), false)
    }

    fun translatable(key: String) {
        MinecraftClient.getInstance().player?.sendMessage(Text.translatable(key))
    }

    fun translatable(key: String, value: String) {
        MinecraftClient.getInstance().player?.sendMessage(Text.translatable(key, value))
    }

    fun translatable(key: String, s: CommandContext<ServerCommandSource>) {
        s.source.sendFeedback(Text.translatable(key), false)
    }

    fun translatable(key: String, value: String, s: CommandContext<ServerCommandSource>) {
        s.source.sendFeedback(Text.translatable(key, value), false)
    }

    fun translatable(key: String, value: MutableText, s: CommandContext<ServerCommandSource>) {
        s.source.sendFeedback(Text.translatable(key, value), false)
    }

    fun translatable(key: String, value: MutableText) {
        MinecraftClient.getInstance().player?.sendMessage(Text.translatable(key, value))
    }

    fun notPlaying() {
        MinecraftClient.getInstance().player?.sendMessage(Text.translatable("rmusic.player.notplaying"))
    }

    fun notPlaying(s: CommandContext<ServerCommandSource>) {
        s.source.sendFeedback(Text.translatable("rmusic.player.notplaying"), false)
    }
}
