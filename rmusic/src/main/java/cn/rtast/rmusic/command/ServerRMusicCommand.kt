/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.command

import cn.rtast.rmusic.entity.payload.QRCodeLoginPacket
import cn.rtast.rmusic.entity.payload.RMusicPayload
import cn.rtast.rmusic.enums.Action
import cn.rtast.rmusic.util.createActionPacket
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.str.encodeToBase64
import cn.rtast.rmusic.util.str.toJson
import com.mojang.brigadier.CommandDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.mcbrawls.slate.Slate.Companion.slate
import net.mcbrawls.slate.tile.Tile.Companion.tile
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.item.Items
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class ServerRMusicCommand : CommandRegistrationCallback {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        dispatcher.register(
            CommandManager.literal("rm-login")
                .executes { context ->
                    val slate = slate {
                        title = Text.literal("选择你的登录方式")
                        tiles {
                            this[1, 2] = tile(Items.NETHERITE_BLOCK) {
                                tooltip("二维码")
                                onGenericClick { slate, _, context ->
                                    context.player.sendMessage(Text.literal("正在获取二维码..."))
                                    scope.launch {
                                        val (key, qrcode) = NCMusic.loginByQRCode()
                                        val qrcodeBase64 = qrcode.encodeToBase64()
                                        val qrcodePacket = QRCodeLoginPacket(key, qrcodeBase64).toJson()
                                        val actionPacket = createActionPacket(Action.LOGIN_QRCODE, qrcodePacket)
                                        val packet = RMusicPayload(actionPacket)
                                        ServerPlayNetworking.send(context.player, packet)
                                    }
                                    slate.close(context.player)
                                }
                            }
                            this[3, 2] = tile(Items.HONEY_BLOCK) {
                                tooltip("邮箱&密码")
                                onGenericClick { slate, _, context ->
                                    context.player.sendMessage(Text.literal("暂不支持此方式登录!"))
                                    slate.close(context.player)
                                }
                            }
                            this[5, 2] = tile(Items.REDSTONE_BLOCK) {
                                tooltip("手机号&密码")
                                onGenericClick { slate, _, context ->
                                    context.player.sendMessage(Text.literal("暂不支持此方式登录!"))
                                    slate.close(context.player)
                                }
                            }
                            this[7, 2] = tile(Items.SLIME_BLOCK) {
                                tooltip("手机号&验证码")
                                onGenericClick { slate, _, context ->
                                    context.player.sendMessage(Text.literal("暂不支持此方式登录!"))
                                    slate.close(context.player)
                                }
                            }
                        }
                    }
                    slate.open(context.source.player!!)
                    0
                }
        )
    }

    private fun openPhoneNumberSlate(player: ServerPlayerEntity) {
        val slate = slate {
            title
        }
        slate.open(player)
        TODO("Not complete yet")
    }
}