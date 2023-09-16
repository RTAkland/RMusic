/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package cn.rtast.rmusic.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

interface BaseCommand : CommandRegistrationCallback {
    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        val rootCommandNode = dispatcher.register(CommandManager.literal("rmusic").then(
            CommandManager.literal("play").then(CommandManager.argument("songId", StringArgumentType.string())
                .executes { this.executePlay(it, StringArgumentType.getString(it, "songid"));1 })
        ).then(CommandManager.literal("stop").executes { this.executeStop(it);1 })
            .then(CommandManager.literal("pause").executes { this.executePause(it);1 })
            .then(CommandManager.literal("resume").executes { this.executeResume(it);1 }).then(
                CommandManager.literal("seek").then(CommandManager.argument("seek-value", IntegerArgumentType.integer())
                    .executes { this.executeSeek(it, IntegerArgumentType.getInteger(it, "seek-value"));1 })
            ).then(
                CommandManager.literal("volume")
                    .then(CommandManager.argument("volume-value", DoubleArgumentType.doubleArg()).executes {
                        this.executeVolume(
                            it, DoubleArgumentType.getDouble(it, "volume-value")
                        );1
                    })
            ).then(CommandManager.literal("login")
                .then(CommandManager.literal("qrcode").executes { this.executeLoginWithQRCode(it);1 }).then(
                    CommandManager.literal("email").then(
                        CommandManager.argument("email", StringArgumentType.string())
                            .then(CommandManager.argument("passwd", StringArgumentType.string()).executes {
                                this.executeLoginWithEmail(
                                    it,
                                    StringArgumentType.getString(it, "email"),
                                    StringArgumentType.getString(it, "passwd")
                                );1
                            })
                    )
                ).then(CommandManager.literal("phone")
                    .then(CommandManager.argument("phone", StringArgumentType.string())
                        .then(CommandManager.argument("passwd", StringArgumentType.string()).executes {
                            this.executeLoginWithCellphone(
                                it,
                                LongArgumentType.getLong(it, "phone"),
                                StringArgumentType.getString(it, "passwd")
                            );1
                        }).executes {
                            this.executeLoginWithCellphone(
                                it, LongArgumentType.getLong(it, "phone"), null
                            );1
                        })
                )
            ).then(CommandManager.literal("logout").executes { this.executeLogout(it);1 }).then(
                CommandManager.literal("verify").then(
                    CommandManager.literal("captcha")
                        .then(CommandManager.argument("captcha", StringArgumentType.string()).executes {
                            this.executeVerifyCaptcha(
                                it, StringArgumentType.getString(it, "captcha")
                            );1
                        })
                ).then(
                    CommandManager.literal("qrcode")
                        .then(CommandManager.argument("qr-key", StringArgumentType.string()).executes {
                            this.executeVerifyQRCode(
                                it, StringArgumentType.getString(it, "qr-key")
                            );1
                        })
                )
            ).then(CommandManager.literal("set-api-host").requires { it.hasPermissionLevel(2) }
                .then(CommandManager.argument("new-host", StringArgumentType.string())
                    .executes { this.executeSetAPIHost(it, StringArgumentType.getString(it, "new-host"));1 }))
        )
        dispatcher.register(CommandManager.literal("rm").redirect(rootCommandNode))
    }

    fun executePlay(source: CommandContext<ServerCommandSource>, songId: String)

    fun executeStop(source: CommandContext<ServerCommandSource>)

    fun executePause(source: CommandContext<ServerCommandSource>)

    fun executeResume(source: CommandContext<ServerCommandSource>)

    fun executeSeek(source: CommandContext<ServerCommandSource>, value: Int)

    fun executeVolume(source: CommandContext<ServerCommandSource>, value: Double)

    fun executeLoginWithEmail(source: CommandContext<ServerCommandSource>, email: String, passwd: String)

    fun executeLoginWithQRCode(source: CommandContext<ServerCommandSource>)

    fun executeLoginWithCellphone(source: CommandContext<ServerCommandSource>, cellphone: Long, passwd: String?)

    fun executeLogout(source: CommandContext<ServerCommandSource>)

    fun executeVerifyQRCode(source: CommandContext<ServerCommandSource>, key: String)

    fun executeVerifyCaptcha(source: CommandContext<ServerCommandSource>, captcha: String)

    fun executeSetAPIHost(source: CommandContext<ServerCommandSource>, newHost: String)
}