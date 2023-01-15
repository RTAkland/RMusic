/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:33
 */

package cn.rtast.rmusic.client.commands

import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.commands.RMusicCommand
import cn.rtast.rmusic.api.Music163
import cn.rtast.rmusic.player.MusicPlayer
import cn.rtast.rmusic.utils.SearchUtil
import com.goxr3plus.streamplayer.enums.Status
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.net.URL

class RMusicCommand : RMusicCommand() {

    private fun checkout(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player == null) {
            RMusicClient.player = MusicPlayer()
        } else {
            RMusicClient.player?.stop()
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.stop")
                    .styled { it.withColor(Formatting.GREEN) }, false
            )
        }
    }

    override fun play(ctx: CommandContext<ServerCommandSource>, id: Int) {
        // 默认从网易云播放
        ctx.source.sendFeedback(
            Text.translatable("rmusic.player.play.waiting")
                .styled { it.withColor(Formatting.GREEN) }, false
        )
        checkout(ctx)  // 检查是否正在播放
        Thread {
            val res = Music163().getSongUrl(id)
            RMusicClient.player?.play(URL(res.url))
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.playing", Text.literal(res.songName)
                    .styled { it.withColor(Formatting.AQUA) })
                    .styled { it.withColor(Formatting.GREEN) }, false
            )
        }.start()
    }

    override fun stop(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status != Status.STOPPED) {
            RMusicClient.player?.stop()
            RMusicClient.player = null
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.stop")
                    .styled { it.withColor(Formatting.GREEN) }, false
            )
        } else {
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.notplaying")
                    .styled { it.withColor(Formatting.RED) }, false
            )
        }
    }

    override fun resume(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status == Status.PAUSED) {
            RMusicClient.player?.resume()
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.resume")
                    .styled { it.withColor(Formatting.GREEN) }, false
            )
        } else {
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.notplaying")
                    .styled { it.withColor(Formatting.RED) }, false
            )
        }
    }

    override fun pause(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status != null) {
            if (RMusicClient.player?.status == Status.PAUSED) {
                ctx.source.sendFeedback(
                    Text.translatable("rmusic.player.pause.off")
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            } else {
                RMusicClient.player?.pause()
                ctx.source.sendFeedback(
                    Text.translatable("rmusic.player.pause")
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            }
        } else {
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.notplaying")
                    .styled { it.withColor(Formatting.RED) }, false
            )
        }
    }

    override fun mute(ctx: CommandContext<ServerCommandSource>) {
        if (RMusicClient.player?.status == Status.PLAYING) {
            if (RMusicClient.player?.mute == true) {
                RMusicClient.player?.mute = false
                ctx.source.sendFeedback(
                    Text.translatable("rmusic.player.mute.off")
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            } else {
                RMusicClient.player?.mute = true
                ctx.source.sendFeedback(
                    Text.translatable("rmusic.player.mute.on")
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            }
        } else {
            ctx.source.sendFeedback(
                Text.translatable("rmusic.player.notplaying")
                    .styled { it.withColor(Formatting.RED) }, false
            )
        }
    }

    override fun setVolume(ctx: CommandContext<ServerCommandSource>, value: Double) {
        RMusicClient.player?.setGain(value)
        ctx.source.sendFeedback(
            Text.translatable("rmusic.player.setvolume", Text.literal(value.toString())
                .styled { it.withColor(Formatting.AQUA) })
                .styled { it.withColor(Formatting.GREEN) }, false
        )
    }

    override fun search163(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        SearchUtil("163").search(ctx, keyword)
    }

    override fun login163(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        ctx.source.sendFeedback(Text.translatable("rmusic.session.netease.login.wait", ctx), false)
        Thread {
            if (Music163().login(email, password)) {
                ctx.source.sendFeedback(
                    Text.translatable("rmusic.session.netease.login.success")
                        .styled { it.withColor(Formatting.GREEN) }, false
                )
            } else {
                ctx.source.sendFeedback(
                    Text.translatable("rmusic.session.netease.login.failure")
                        .styled { it.withColor(Formatting.RED) }, false
                )
            }
        }.start()
    }

    override fun logout163(ctx: CommandContext<ServerCommandSource>) {
        ctx.source.sendFeedback(
            Text.translatable("rmusic.session.netease.logout.wait")
                .styled { it.withColor(Formatting.GREEN) }, false
        )
        if (Music163().logout()) {
            ctx.source.sendFeedback(
                Text.translatable("rmusic.session.netease.logout.success")
                    .styled { it.withColor(Formatting.GREEN) },
                false
            )
        } else {
            ctx.source.sendFeedback(
                Text.translatable("rmusic.session.netease.logout.failure")
                    .styled { it.withColor(Formatting.RED) },
                false
            )
        }
    }
}