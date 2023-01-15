/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/12 17:37
 */

package cn.rtast.rmusic.utils

import cn.rtast.rmusic.api.Music163
import com.mojang.brigadier.context.CommandContext
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class SearchUtil(private val platform: String) {
    fun search(msg: ClientPlayerEntity, keyword: String) {
        if (platform == "163") {
            msg.sendMessage(
                Text.translatable("rmusic.api.search.netease", Text.literal(keyword)
                    .styled { it.withColor(Formatting.AQUA) })
                    .styled { it.withColor(Formatting.GREEN) }, false
            )
            Thread {
                val result = Music163().search(keyword)
                result.forEach {
                    msg.sendMessage(StyleUtil.resultStyle(it), false)
                }
                msg.sendMessage(
                    Text.translatable("rmusic.api.search.tip", Text.literal(result.size.toString())
                        .styled { it.withColor(Formatting.AQUA) })
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            }.start()
        }
    }

    fun search(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        if (platform == "163") {
            ctx.source.sendFeedback(
                Text.translatable("rmusic.api.search.netease", Text.literal(keyword)
                    .styled { it.withColor(Formatting.AQUA) })
                    .styled { it.withColor(Formatting.GREEN) }, false
            )
            Thread {
                val result = Music163().search(keyword)
                result.forEach {
                    ctx.source.sendFeedback(StyleUtil.resultStyle(it), false)
                }
                ctx.source.sendFeedback(
                    Text.translatable("rmusic.api.search.tip", Text.literal(result.size.toString())
                        .styled { it.withColor(Formatting.AQUA) })
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            }.start()
        }
    }
}