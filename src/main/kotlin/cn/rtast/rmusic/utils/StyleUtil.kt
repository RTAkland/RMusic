package cn.rtast.rmusic.utils

import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object StyleUtil {
    fun resultStyle(msg: String): MutableText {
        val id = msg.split("|").last()
        val nameText = Text.literal(msg.split("|").first())
            .styled {
                it.withColor(Formatting.YELLOW)
            }
        val artistsText = Text.literal(msg.split("|")[1])
            .styled {
                it.withColor(Formatting.AQUA)
            }
        val playText = Text.literal("[▶]").styled {
            it.withColor(Formatting.GREEN)
                .withClickEvent(
                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rmusic play $id")
                ).withHoverEvent(
                    HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("rmusic.chat.play.click", id))
                ).withInsertion(id)
        }
        return Text.translatable("rmusic.chat.songinfo", nameText, artistsText, playText)
    }

    fun greenStyle(msg: String): MutableText {
        return Text.literal(msg).styled { it.withColor(Formatting.GREEN) }
    }
}