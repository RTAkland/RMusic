package cn.rtast.rmusic.commands

import cn.rtast.rmusic.music.NeteaseMusic
import cn.rtast.rmusic.utils.Message
import cn.rtast.rmusic.utils.MusicPlayer
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.command.CommandManager.*

class RMusicCommand {
    private var music: MusicPlayer? = null

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("rmusic")
                .then(
                    literal("play")
                        .then(
                            literal("url")
                                .then(
                                    argument("url", string())
                                        .executes { playMusic(it, getString(it, "url"));1 }
                                )
                        )
                        .then(
                            argument("id", string())
                                .executes { playMusic(it, getString(it, "id"), "netease");1 }
                        )
                )
                .then(
                    literal("stop")
                        .executes { stopMusic(it);1 }
                )
                .then(
                    literal("resume")
                        .executes { resumeMusic(it);1 }
                )
                .then(
                    literal("pause")
                        .executes { pauseMusic(it);1 }
                )
                .then(
                    literal("search")
                        .then(
                            literal("netease")
                                .then(
                                    argument("keyword", string())
                                        .executes { searchNetease(it, getString(it, "keyword"));1 }
                                )
                        )
                )
        )
    }

    private fun playMusic(ctx: CommandContext<ServerCommandSource>, id: String, platform: String) {
        if (ctx.source.isExecutedByPlayer) {
            if (music == null) {
                music = MusicPlayer()
                Message().m(ctx, "正在获取音乐源请稍后! 如果卡住请先stop再重试!")
                Thread { music?.playMusic(id, platform) }.start()
            } else {
                Message().m(ctx, "已停止播放正在播放的音乐, 新的音乐ID: $id")
                music?.stop()
                Thread { music?.playMusic(id, platform) }.start()
            }
        } else {
            Message().notClient(ctx)
        }
    }

    private fun playMusic(ctx: CommandContext<ServerCommandSource>, url: String) {
        if (ctx.source.isExecutedByPlayer) {
            if (music == null) {
                music = MusicPlayer()
                Message().m(ctx, "正在请求URL")
                Thread { music?.playMusic(url) }.start()
            } else {
                Message().m(ctx, "已停止播放正在播放的音乐, 正在播放新的URL音乐...")
                music?.stop()
                Thread { music?.playMusic(url) }.start()
            }
        } else {
            Message().notClient(ctx)
        }
    }

    private fun stopMusic(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.isExecutedByPlayer) {
            if (music != null) {
                music?.stop()
                music = null
                Message().m(ctx, "已停止播放!")
            } else {
                Message().notPlayed(ctx)
            }
        } else {
            Message().notClient(ctx)
        }
    }

    private fun resumeMusic(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.isExecutedByPlayer) {
            if (music != null) {
                music?.resume()
                Message().m(ctx, "继续播放...")
            } else {
                Message().notPlayed(ctx)
            }
        } else {
            Message().notClient(ctx)
        }
    }

    private fun pauseMusic(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.isExecutedByPlayer) {
            if (music != null) {
                music?.pause()
                Message().m(ctx, "已暂停!")
            } else {
                Message().notPlayed(ctx)
            }
        } else {
            Message().notClient(ctx)
        }
    }

    private fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        Message().m(ctx, "正在搜索中请耐心等候...")
        Thread {
            val data = NeteaseMusic().search(keyword)
            data.result.songs.forEach {
                var res = "${it.name} "
                it.artists.forEach { ar ->
                    res += " ${ar.name} "
                }
                res += it.id
                Message().m(ctx, res)
            }
        }.start()
    }
}