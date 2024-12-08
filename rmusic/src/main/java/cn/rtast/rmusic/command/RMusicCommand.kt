/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.RMusicServer
import cn.rtast.rmusic.entity.PlayList
import cn.rtast.rmusic.entity.payload.outbound.PlayMusicOutbound
import cn.rtast.rmusic.entity.payload.outbound.SearchResultOutbound
import cn.rtast.rmusic.entity.payload.outbound.ShareMusicOutbound
import cn.rtast.rmusic.entity.payload.side.Pause2Side
import cn.rtast.rmusic.entity.payload.side.ResumePlay2Side
import cn.rtast.rmusic.entity.payload.side.StopPlay2Side
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.enums.MusicPlatform
import cn.rtast.rmusic.scope
import cn.rtast.rmusic.util.mc.*
import cn.rtast.rmusic.util.music.KuGouMusic
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.music.QQMusic
import cn.rtast.rmusic.util.toMinuteSecond
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.io.File

class RMusicCommand : CommandRegistrationCallback {

    companion object {
        val playerPlayList = mutableMapOf<ServerPlayerEntity, MutableList<PlayList>>()

        fun executePlayMusic(
            songId: String,
            platform: MusicPlatform,
            context: ServerPlayerEntity
        ) {
            context.sendFeedback(Text.literal("正在获取歌曲信息..."))
            when (platform) {
                MusicPlatform.Netease -> {
                    scope.launch {
                        try {
                            val songUrl = NCMusic.getSongUrl(songId.toLong())
                            val songDetail = NCMusic.getSongDetail(songId.toLong())
                            val lyric = NCMusic.getLyric(songId.toLong())
                            PlayMusicOutbound(
                                songUrl,
                                songId,
                                songDetail.name,
                                songDetail.artists,
                                songDetail.cover,
                                lyric,
                                songDetail.duration.toMinuteSecond()
                            ).createActionPacket(IntentAction.PLAY).sendToClient(context)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            context.sendFeedback(Text.literal("获取歌曲信息失败: ${e.message}"))
                        }
                    }
                }

                MusicPlatform.QQ -> {
                    scope.launch {
                        try {
                            val songUrl = QQMusic.getSongUrl(songId)
                            val songDetail = QQMusic.getSongInfo(songId)
                            val lyric = QQMusic.getLyric(songId)
                            PlayMusicOutbound(
                                songUrl,
                                songId,
                                songDetail.name,
                                songDetail.artists,
                                songDetail.cover,
                                lyric,
                                songDetail.duration.toMinuteSecond()
                            ).createActionPacket(IntentAction.PLAY).sendToClient(context)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            context.sendFeedback(Text.literal("获取歌曲信息失败: ${e.message}"))
                        }
                    }
                }

                MusicPlatform.KuGou -> {
                    scope.launch {
                        try {
                            val songUrl = KuGouMusic.getSongUrl(songId)
                            val songDetail = KuGouMusic.getSongInfo(songId)
                            val lyric = mapOf<Int, String>()
                            PlayMusicOutbound(
                                songUrl,
                                songId,
                                songDetail.name,
                                songDetail.artists,
                                songDetail.cover,
                                lyric,
                                songDetail.duration.toMinuteSecond()
                            ).createActionPacket(IntentAction.PLAY).sendToClient(context)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            context.sendFeedback(Text.literal("获取歌曲信息失败: ${e.message}"))
                        }
                    }
                }
            }
        }

    }

    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        val shortNode = dispatcher.register(
            CommandManager.literal("rm")
                .then(
                    CommandManager.literal("search")
                        .then(
                            CommandManager.literal("netease")
                                .then(
                                    CommandManager.argument("keyword", StringArgumentType.string())
                                        .executes { context ->
                                            val keyword = context.getArgument("keyword", String::class.java)
                                            scope.launch {
                                                this@RMusicCommand.executeSearch(
                                                    keyword,
                                                    MusicPlatform.Netease,
                                                    context
                                                )
                                            }
                                            0
                                        }
                                )
                        ).then(
                            CommandManager.literal("qq")
                                .then(
                                    CommandManager.argument("qq-keyword", StringArgumentType.string())
                                        .executes { context ->
                                            val keyword = context.getArgument("qq-keyword", String::class.java)
                                            scope.launch {
                                                this@RMusicCommand.executeSearch(keyword, MusicPlatform.QQ, context)
                                            }
                                            0
                                        }
                                )
                        ).then(
                            CommandManager.literal("kugou")
                                .then(
                                    CommandManager.argument("kugou-keyword", StringArgumentType.string())
                                        .executes { context ->
                                            val keyword = context.getArgument("kugou-keyword", String::class.java)
                                            scope.launch {
                                                this@RMusicCommand.executeSearch(keyword, MusicPlatform.KuGou, context)
                                            }
                                            0
                                        }
                                )
                        )
                ).then(
                    CommandManager.literal("play")
                        .then(
                            CommandManager.literal("netease")
                                .then(
                                    CommandManager.argument("songId", StringArgumentType.string())
                                        .executes { context ->
                                            val songId = context.getArgument("songId", String::class.java)
                                            executePlayMusic(
                                                songId,
                                                MusicPlatform.Netease,
                                                context.source.player!!
                                            )
                                            0
                                        }
                                )
                        ).then(
                            CommandManager.literal("qq")
                                .then(
                                    CommandManager.argument("qq-song-id", StringArgumentType.string())
                                        .executes { context ->
                                            val songId = context.getArgument("qq-song-id", String::class.java)
                                            executePlayMusic(songId, MusicPlatform.QQ, context.source.player!!)
                                            0
                                        }
                                )
                        ).then(
                            CommandManager.literal("kugou")
                                .then(
                                    CommandManager.argument("kugou-song-id", StringArgumentType.string())
                                        .executes { context ->
                                            val songId = context.getArgument("kugou-song-id", String::class.java)
                                            executePlayMusic(songId, MusicPlatform.KuGou, context.source.player!!)
                                            0
                                        }
                                )
                        )
                ).then(
                    CommandManager.literal("pause")
                        .executes { context ->
                            Pause2Side().createActionPacket(IntentAction.PAUSE).sendToClient(context)
                        }
                ).then(
                    CommandManager.literal("resume")
                        .executes { context ->
                            ResumePlay2Side().createActionPacket(IntentAction.RESUME).sendToClient(context)
                        }
                ).then(
                    CommandManager.literal("stop")
                        .executes { context ->
                            StopPlay2Side().createActionPacket(IntentAction.STOP).sendToClient(context)
                        }
                ).then(
                    CommandManager.literal("login")
                        .executes { context ->
                            context.executeOpenLoginMenu()
                            0
                        }
                        .then(
                            CommandManager.literal("netease")
                                .then(
                                    CommandManager.literal("confirm")
                                        .then(
                                            CommandManager.argument("netease-qrcode-key", StringArgumentType.string())
                                                .executes { context ->
                                                    val key =
                                                        context.getArgument("netease-qrcode-key", String::class.java)
                                                    this.executeCheckQRCodeStatus(key, MusicPlatform.Netease, context)
                                                    0
                                                }
                                        )
                                )
                        ).then(
                            CommandManager.literal("kugou")
                                .then(
                                    CommandManager.literal("confirm")
                                        .then(
                                            CommandManager.argument("kugou-qrcode-key", StringArgumentType.string())
                                                .executes { context ->
                                                    val key =
                                                        context.getArgument("kugou-qrcode-key", String::class.java)
                                                    this.executeCheckQRCodeStatus(key, MusicPlatform.KuGou, context)
                                                    0
                                                }
                                        )
                                )
                        )
                ).then(
                    CommandManager.literal("clean-cache")
                        .then(
                            CommandManager.literal("confirm")
                                .executes { context ->
                                    this.executeCleanCacheConfirm(context)
                                    0
                                }
                        ).executes { context ->
                            this.executeCleanCache(context)
                            0
                        }
                ).then(
                    CommandManager.literal("reload").requires { it.hasPermissionLevel(3) }
                        .executes { context ->
                            RMusicServer.configManager.reload()
                            RMusicServer.cookieManager.reload()
                            context.sendFeedback(Text.literal("已重新加载配置文件"))
                            0
                        }
                ).then(
                    CommandManager.literal("netease-share")
                        .then(
                            CommandManager.argument("songId", StringArgumentType.string())
                                .executes { context ->
                                    val songId = context.getArgument("songId", String::class.java)
                                    this.executeShareMusic(songId, MusicPlatform.Netease, context)
                                    0
                                }
                        )
                ).then(
                    CommandManager.literal("qq-share")
                        .then(
                            CommandManager.argument("qq-song-mid", StringArgumentType.string())
                                .executes { context ->
                                    val songMid = context.getArgument("qq-song-mid", String::class.java)
                                    this.executeShareMusic(songMid, MusicPlatform.QQ, context)
                                    0
                                }
                        )
                ).then(
                    CommandManager.literal("kugou-share")
                        .then(
                            CommandManager.argument("kugou-song-id", StringArgumentType.string())
                                .executes { context ->
                                    val songId = context.getArgument("kugou-song-id", String::class.java)
                                    this.executeShareMusic(songId, MusicPlatform.KuGou, context)
                                    0
                                }
                        )
                ).then(
                    CommandManager.literal("playlist")
                        .executes { context ->
                            this.executeGetPlayList(context)
                            0
                        }
                        .then(
                            CommandManager.literal("add")
                                .then(
                                    CommandManager.argument("playlist-song-id", StringArgumentType.string())
                                        .then(
                                            CommandManager.argument("platform", StringArgumentType.word())
                                                .suggests { _, builder ->
                                                    MusicPlatform.entries.forEach {
                                                        builder.suggest(it.platform)
                                                    }
                                                    builder.buildFuture()
                                                }.executes { context ->
                                                    val songId =
                                                        context.getArgument("playlist-song-id", String::class.java)
                                                    val rawPlatform =
                                                        context.getArgument("platform", String::class.java)
                                                    this.executeAddToPlaylist(songId, rawPlatform, context)
                                                    0
                                                }
                                        )
                                )
                        )
                )
        )
        dispatcher.register(CommandManager.literal("rmusic").redirect(shortNode))
    }

    private fun executeGetPlayList(context: CommandContext<ServerCommandSource>) {
        val playList = playerPlayList.getOrElse(context.source.player!!) {
            mutableListOf()
        }
        context.sendFeedback(
            Text.literal("目前播放列表有")
                .append(Text.literal(playList.size.toString()).styled {
                    it.withColor(Formatting.GREEN)
                }).append("首音乐")
        )
    }

    private fun executeAddToPlaylist(
        songId: String,
        rawPlatform: String,
        context: CommandContext<ServerCommandSource>
    ) {
        val platform = MusicPlatform.forName(rawPlatform)
        if (platform == null) {
            context.sendFeedback(Text.literal("暂时不支持${rawPlatform}哦"))
        } else {
            if (playerPlayList[context.source.player!!] == null) {
                playerPlayList[context.source.player!!] = mutableListOf()
            }
            val playList = playerPlayList[context.source.player!!]!!
            playList.add(PlayList(songId, platform))
            context.sendFeedback(
                Text.literal("成功添加歌曲到播放列表,目前播放列表有")
                    .append(Text.literal(playList.size.toString()).styled {
                        it.withColor(Formatting.GREEN)
                    }).append("首音乐")
            )
        }
    }

    private fun executeSearch(keyword: String, platform: MusicPlatform, context: CommandContext<ServerCommandSource>) {
        context.sendFeedback(Text.literal("正在搜索: ").append(Text.literal(keyword).styled {
            it.withColor(Formatting.AQUA)
        }))
        try {
            val searchResult = when (platform) {
                MusicPlatform.Netease -> {
                    NCMusic.search(keyword).take(8).map {
                        SearchResultOutbound.Result(
                            it.id,
                            it.name,
                            it.artists,
                            MusicPlatform.Netease
                        )
                    }
                }

                MusicPlatform.QQ -> {
                    QQMusic.search(keyword).take(8).map {
                        SearchResultOutbound.Result(
                            it.id,
                            it.name,
                            it.artists,
                            MusicPlatform.QQ
                        )
                    }
                }

                MusicPlatform.KuGou -> {
                    KuGouMusic.search(keyword).take(8).map {
                        SearchResultOutbound.Result(
                            it.id,
                            it.name,
                            it.artists,
                            MusicPlatform.KuGou
                        )
                    }
                }
            }
            val text = Text.literal("搜索到如下结果:")
            context.sendFeedback(text)
            searchResult.forEach { r ->
                val songText =
                    Text.literal("歌曲名: ").styled {
                        it.withColor(Formatting.GREEN)
                    }.append(Text.literal("${r.songName} ").styled {
                        it.withColor(Formatting.AQUA)
                    }).append(" 歌手: ")
                        .append(Text.literal(r.artistName).styled {
                            it.withColor(Formatting.AQUA)
                        })
                val playButton = Text.literal(" [▶]").styled { style ->
                    style.withColor(Formatting.DARK_PURPLE)
                        .withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Text.literal("点击播放音乐")
                                    .styled { it.withColor(Formatting.YELLOW) })
                        ).withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/rm play ${r.platform.platform} ${r.id.ifEmpty { "000000" }}"
                            )
                        )
                }
                val addToPlayListButton = Text.literal(" [+]").styled { style ->
                    style.withColor(Formatting.GOLD)
                        .withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Text.literal("点击添加到播放列表中").styled {
                                    it.withColor(Formatting.YELLOW)
                                })
                        ).withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/rm playlist add ${r.id.ifEmpty { "000000" }} ${r.platform.platform}"
                            )
                        )
                }
                val shareButton = Text.literal(" [↗]").styled { style ->
                    style.withColor(Formatting.AQUA)
                        .withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/rm ${r.platform.platform}-share ${r.id.ifEmpty { "000000" }}"
                            )
                        ).withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Text.literal("点击分享给服务器内的其他玩家\n")
                                    .append(
                                        Text.literal("注意: 只有服务器安装了RMusic并且其他玩家安装了RMusic客户端模组的玩家才能收到分享消息")
                                            .styled { it.withColor(Formatting.RED) })
                            )
                        )
                }
                songText.append(playButton)
                songText.append(shareButton)
                songText.append(addToPlayListButton)
                context.sendFeedback(songText)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            context.sendFeedback(Text.literal("搜索失败: ${e.message}"))
        }
    }

    private fun CommandContext<ServerCommandSource>.executeOpenLoginMenu() {
        openMusicPlatformMenu(this)
    }

    private fun executeCheckQRCodeStatus(
        key: String,
        platform: MusicPlatform,
        context: CommandContext<ServerCommandSource>
    ) {
        scope.launch {
            Renderer.loadQRCode = false
            context.sendFeedback(Text.literal("正在检查登录状态中"))
            try {
                when (platform) {
                    MusicPlatform.Netease -> {
                        val cookie = NCMusic.checkQRCodeStatus(key)
                        if (cookie != null) {
                            RMusicServer.cookieManager.login(cookie, MusicPlatform.Netease)
                            val accountInfo = NCMusic.getUserAccount()
                            context.sendFeedback(Text.literal("登陆成功, 用户名: $accountInfo"))
                        } else {
                            context.sendFeedback(Text.literal("验证失败, 请检查是否扫码并成功登录"))
                        }
                    }

                    MusicPlatform.QQ -> TODO()
                    MusicPlatform.KuGou -> {
                        val cookie = KuGouMusic.checkQRCodeStatus(key)
                        if (cookie != null) {
                            RMusicServer.cookieManager.login(cookie.first, MusicPlatform.KuGou)
                            context.sendFeedback(Text.literal("登陆成功, 用户名: ${cookie.second}"))
                        } else {
                            context.sendFeedback(Text.literal("验证失败, 请检查是否扫码并成功登录"))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                context.sendFeedback(Text.literal("验证失败, 错误原因: ${e.message}"))
            }
        }
    }

    private fun executeShareMusic(
        songId: String,
        platform: MusicPlatform,
        context: CommandContext<ServerCommandSource>
    ) {
        context.sendFeedback(Text.literal("正在获取分享音乐所需的必要信息..."))
        when (platform) {
            MusicPlatform.Netease -> {
                scope.launch {
                    try {
                        val songUrl = NCMusic.getSongUrl(songId.toLong())
                        val songDetail = NCMusic.getSongDetail(songId.toLong())
                        ShareMusicOutbound(
                            songDetail.name,
                            songId,
                            songDetail.artists,
                            songUrl,
                            context.source.player!!.name.string,
                            MusicPlatform.Netease
                        ).createActionPacket(IntentAction.SHARE).sendToClient(context)
                    } catch (e: Exception) {
                        context.sendFeedback(Text.literal("分享失败, 错误原因: ${e.message}"))
                    }
                }
            }

            MusicPlatform.QQ -> {
                scope.launch {
                    try {
                        val songUrl = QQMusic.getSongUrl(songId)
                        val songDetail = QQMusic.getSongInfo(songId)
                        ShareMusicOutbound(
                            songDetail.name,
                            songId,
                            songDetail.artists,
                            songUrl,
                            context.source.player!!.name.string,
                            MusicPlatform.QQ
                        ).createActionPacket(IntentAction.SHARE).sendToClient(context)
                    } catch (e: Exception) {
                        context.sendFeedback(Text.literal("分享失败, 错误原因: ${e.message}"))
                    }
                }
            }

            MusicPlatform.KuGou -> {
                scope.launch {
                    try {
                        val songUrl = KuGouMusic.getSongUrl(songId)
                        val songDetail = KuGouMusic.getSongInfo(songId)
                        ShareMusicOutbound(
                            songDetail.name,
                            songId,
                            songDetail.artists,
                            songUrl,
                            context.source.player!!.name.string,
                            MusicPlatform.KuGou
                        ).createActionPacket(IntentAction.SHARE).sendToClient(context)
                    } catch (e: Exception) {
                        context.sendFeedback(Text.literal("分享失败, 错误原因: ${e.message}"))
                    }
                }
            }
        }
    }

    private fun executeCleanCache(context: CommandContext<ServerCommandSource>) {
        context.sendFeedback(
            Text.literal("本操作会清空所有已缓存到本地的音频文件, 点击").append(
                Text.literal("[这里]").styled { style ->
                    style.withColor(Formatting.GREEN)
                        .withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Text.literal("点击确认").styled {
                                    it.withColor(Formatting.GREEN)
                                })
                        )
                        .withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/rm clean-cache confirm"
                            )
                        )
                }).append("来确认操作")
        )
    }

    private fun executeCleanCacheConfirm(context: CommandContext<ServerCommandSource>) {
        val files = File("./config/rmusic/cache")
            .listFiles()?.filter { it.isFile } ?: emptyList()
        files.forEach { file -> file.delete() }
        context.sendFeedback(
            Text.literal("已删除").append(
                Text.literal("${files.size}")
                    .styled {
                        it.withColor(Formatting.RED)
                            .withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Text.literal(files.joinToString(separator = "\n") { file -> file.name })
                                )
                            )
                    }).append("个文件")
        )
    }
}