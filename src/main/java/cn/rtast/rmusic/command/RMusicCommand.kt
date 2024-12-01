/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.util.NCMusic
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class RMusicCommand : ClientCommandRegistrationCallback {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<FabricClientCommandSource>("rm")
                .then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("search")
                        .then(
                            argument("keyword", StringArgumentType.string()).executes { context ->
                                val keyword = context.getArgument("keyword", String::class.java)
                                scope.launch {
                                    val result = NCMusic.search(keyword)
                                    val text = Text.empty().apply { append("搜索到如下结果: \n") }
                                    result.take(7).forEach { result ->
                                        val formattedText = Text.of("${result.name} - ${result.artists}")
                                        text.append(formattedText)
                                        text.append("[▶]").styled {
                                            it.withColor(Formatting.GREEN).withClickEvent(
                                                ClickEvent(
                                                    ClickEvent.Action.RUN_COMMAND,
                                                    "/rm play ${result.id}"
                                                )
                                            ).withHoverEvent(
                                                HoverEvent(
                                                    HoverEvent.Action.SHOW_TEXT,
                                                    Text.empty().apply {
                                                        append("点击播放").styled { it.withColor(Formatting.GREEN) }
                                                    })
                                            )
                                        }
                                        text.append(Text.of("\n"))
                                    }
                                    context.source.sendFeedback(text)
                                }
                                0
                            }
                        )
                ).then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("play")
                        .then(
                            argument("songId", LongArgumentType.longArg(0)).executes { context ->
                                val songId = context.getArgument("songId", Long::class.java)
                                scope.launch {
                                    val songUrl = NCMusic.getSongUrl(songId)
                                    val lyric = NCMusic.getLyric(songId)
                                    RMusic.player.playMusic(songUrl, lyric)
                                }
                                0
                            }
                        )
                )
        )
    }
}