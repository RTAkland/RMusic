/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/12 17:37
 */

package cn.rtast.rmusic.utils

import cn.rtast.rmusic.music.Music163
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class SearchUtil {
    fun search(ctx: CommandContext<ServerCommandSource>, keyword: String) {
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