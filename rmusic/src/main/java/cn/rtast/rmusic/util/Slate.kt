/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/6
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.entity.KeyboardState
import cn.rtast.rmusic.entity.payload.outbound.QRCodeLoginOutbound
import cn.rtast.rmusic.enums.IntentAction
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.str.encodeToBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.mcbrawls.slate.Slate.Companion.slate
import net.mcbrawls.slate.screen.slot.ClickType
import net.mcbrawls.slate.tile.Tile.Companion.tile
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

private val scope = CoroutineScope(Dispatchers.IO)

private val numberKeyboardGrid = mapOf(
    (0 to 3) to (1 to '!'),
    (1 to 3) to (2 to '@'),
    (2 to 3) to (3 to '#'),
    (0 to 4) to (4 to '$'),
    (1 to 4) to (5 to '%'),
    (2 to 4) to (6 to '^'),
    (0 to 5) to (7 to '&'),
    (1 to 5) to (8 to '*'),
    (2 to 5) to (9 to '('),
    (3 to 4) to (0 to ')'),
)

private val specialCharGrid = mapOf(
    (4 to 3) to ('~' to "`"),
    (4 to 4) to ('-' to "_"),
    (4 to 5) to ('=' to "+"),
    (5 to 3) to ('~' to "`"),
    (5 to 4) to ('[' to "{"),
    (5 to 5) to ('[' to "}"),
    (6 to 3) to ('\\' to "|"),
    (6 to 4) to (';' to ":"),
    (6 to 5) to ('\'' to "\""),
    (7 to 3) to (',' to "<"),
    (7 to 4) to ('.' to ">"),
    (7 to 5) to ('/' to "?"),
)

fun openMusicPlatformMenu(player: ServerPlayerEntity) {
    player.playPLing()
    val slate = slate {
        title = Text.literal("选择需要登陆的平台")
        tiles {
            this[3, 2] = tile(Items.RED_CONCRETE) {
                tooltip("网易云音乐")
                onGenericClick { slate, _, slateContext ->
                    openNeteaseLoginTypeMenu(slateContext.player)
                    slate.close(slateContext.player)
                }
            }
            this[5, 2] = tile(Items.LIME_CONCRETE) {
                tooltip("QQ音乐")
                onGenericClick { slate, _, slateContext ->
                    slateContext.player.sendPrefixMessage(Text.literal("暂不支持QQ音乐"))
                    slate.close(player)
                }
            }
        }
    }
    slate.open(player)
}

fun openNeteaseLoginTypeMenu(player: ServerPlayerEntity) {
    player.playPLing()
    val slate = slate {
        title = Text.literal("选择登陆方式")
        tiles {
            this[1, 2] = tile(Items.NETHERITE_BLOCK) {
                tooltip("二维码")
                onGenericClick { slate, _, slateContext ->
                    slateContext.clickType
                    player.playButtonSound()
                    slateContext.player.sendMessage(Text.literal("正在获取二维码..."))
                    scope.launch {
                        val (key, qrcode) = NCMusic.loginByQRCode()
                        QRCodeLoginOutbound(key, qrcode.encodeToBase64())
                            .createActionPacket(IntentAction.LOGIN)
                            .sendToClient(player)
                    }
                    slate.close(slateContext.player)
                }
            }
            this[3, 2] = tile(Items.HONEY_BLOCK) {
                tooltip("邮箱&密码")
                onGenericClick { slate, _, slateContext ->
                    player.playButtonSound()
                    slateContext.player.sendPrefixMessage(Text.literal("暂不支持此方式登录!"))
                    slate.close(slateContext.player)
                }
            }
            this[5, 2] = tile(Items.REDSTONE_BLOCK) {
                tooltip("手机号&密码")
                onGenericClick { slate, _, slateContext ->
                    player.playButtonSound()
                    slateContext.player.sendPrefixMessage(Text.literal("暂不支持此方式登录!"))
                    slate.close(slateContext.player)
                }
            }
            this[7, 2] = tile(Items.SLIME_BLOCK) {
                tooltip("手机号&验证码")
                onGenericClick { slate, _, slateContext ->
                    player.playButtonSound()
                    slateContext.player.sendPrefixMessage(Text.literal("暂不支持此方式登录!"))
                    slate.close(slateContext.player)
                }
            }
            this[8, 5] = tile(Items.PLAYER_HEAD) {
                tooltip("返回")
                onGenericClick { _, _, slateContext ->
                    openMusicPlatformMenu(slateContext.player)
                }
            }
//            this[8, 4] = tile(Items.ACACIA_BOAT) {
//                onGenericClick { _, _, slateContext ->
//                    openKeyboardMenu(slateContext.player, null)
//                }
//            }
        }
    }
    slate.open(player)
}


fun openKeyboardMenu(player: ServerPlayerEntity, lastState: KeyboardState?) {
    var currentState = lastState ?: KeyboardState("")
    val baseTitle = Text.literal("输入你的账号和密码")
    if (lastState != null) {
        baseTitle.append(Text.literal("你输入的信息为: ${currentState.input}"))
    }
    val slate = slate {
        tiles {
            title = baseTitle
            ('a'..'z').mapIndexed { index, c ->
                val row = index % 9
                val col = index / 9
                this[row, col] = tile(Items.GREEN_DYE) {
                    tooltip("点击输入$c 右键点击输入${c.uppercaseChar()}")
                    onGenericClick { _, _, slateContext ->
                        currentState = if (slateContext.clickType == ClickType.RIGHT) {
                            KeyboardState("${currentState.input}${c.uppercaseChar()}")
                        } else {
                            KeyboardState("${currentState.input}$c")
                        }
                        openKeyboardMenu(slateContext.player, currentState)
                    }
                }
            }
            numberKeyboardGrid.forEach { (k, v) ->
                this[k.first, k.second] = tile(Items.RED_DYE) {
                    tooltip("点击输入${v.first} 右键点击输入${v.second}")
                    onGenericClick { _, _, slateContext ->
                        currentState = if (slateContext.clickType == ClickType.RIGHT) {
                            KeyboardState("${currentState.input}${v.second}")
                        } else {
                            KeyboardState("${currentState.input}${v.first}")
                        }
                        openKeyboardMenu(slateContext.player, currentState)
                    }
                }
            }

            specialCharGrid.forEach { (k, v) ->
                this[k.first, k.second] = tile(Items.BLUE_DYE) {
                    tooltip("点击输入${v.first} 右键点击输入${v.second}")
                    onGenericClick { _, _, slateContext ->
                        currentState = if (slateContext.clickType == ClickType.RIGHT) {
                            KeyboardState("${currentState.input}${v.second}")
                        } else {
                            KeyboardState("${currentState.input}${v.first}")
                        }
                        openKeyboardMenu(slateContext.player, currentState)
                    }
                }
            }

            this[1, 8] = tile(Items.RED_SAND) {
                tooltip("删除")
                onGenericClick { _, _, slateContext ->
                    currentState = KeyboardState(currentState.input.dropLast(1))
                    openKeyboardMenu(slateContext.player, currentState)
                }
            }

            this[2, 8] = tile(Items.GREEN_WOOL) {
                tooltip("确认")
                onGenericClick { _, _, slateContext ->
                    println(currentState.input)
                }
            }

            this[4, 8] = tile(Items.RED_TERRACOTTA) {
                tooltip("关闭")
                onGenericClick { slate, _, slateContext ->
                    slate.close(slateContext.player)
                }
            }
        }
    }
    slate.open(player)
}