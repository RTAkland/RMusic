/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.utils

import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object StyleUtil {
    fun resultStyle(msg: String): MutableText {
        val songName = msg.split("^").first()
        val artists = msg.split("^")[1]
        val id = msg.split("^").last()
        val nameText = Text.literal(songName)
            .styled {
                it.withColor(Formatting.YELLOW)
            }
        val artistsText = Text.literal(artists)
            .styled {
                it.withColor(Formatting.AQUA)
            }
        val playButtonText = Text.literal("[▶]").styled {
            it.withColor(Formatting.GREEN)
                .withClickEvent(
                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rmusic play $id")
                ).withHoverEvent(
                    HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.play.click", songName)
                        .styled { s ->
                            s.withColor(Formatting.LIGHT_PURPLE)
                        })
                ).withInsertion(id)
        }
        return Text.translatable("chat.songinfo", nameText, artistsText, playButtonText)
    }
}