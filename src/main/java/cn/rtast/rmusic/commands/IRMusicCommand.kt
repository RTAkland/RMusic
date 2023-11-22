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
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

interface IRMusicCommand : CommandRegistrationCallback {
    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment,
    ) {
        val node = dispatcher.register(CommandManager.literal("rmusic").then(
            CommandManager.literal("play").then(CommandManager.argument("song-id", StringArgumentType.string())
                .executes { this.executePlay(it, StringArgumentType.getString(it, "song-id"));1 })
        ).then(CommandManager.literal("stop").executes { this.executeStop(it);1 })
            .then(CommandManager.literal("pause").executes { this.executePause(it);1 })
            .then(CommandManager.literal("resume").executes { this.executeResume(it);1 })
            .then(CommandManager.literal("mute").executes { this.executeMute(it);1 }).then(
                CommandManager.literal("login").requires { it.hasPermissionLevel(2) }.then(
                    CommandManager.literal("cellphone").then(
                        CommandManager.argument("cellphone-number", StringArgumentType.string())
                            .then(CommandManager.argument("cellphone-password", StringArgumentType.string()).executes {
                                    this.executePhoneLogin(
                                        it, StringArgumentType.getString(
                                            it, "cellphone-number"
                                        ), StringArgumentType.getString(it, "cellphone-password")
                                    );1
                                })
                    ).then(
                        CommandManager.literal("captcha").then(
                            CommandManager.literal("send").then(CommandManager.argument(
                                "cellphone-captcha", StringArgumentType.string()
                            ).executes {
                                this.executeSendCaptcha(
                                    it, StringArgumentType.getString(
                                        it, "cellphone-captcha"
                                    )
                                );1
                            })
                        ).then(
                            CommandManager.literal("verify").then(
                                CommandManager.argument(
                                    "cellphone-verify", StringArgumentType.string()
                                ).then(CommandManager.argument(
                                    "captcha", StringArgumentType.string()
                                ).executes {
                                    this.executeVerifyCaptcha(
                                        it, StringArgumentType.getString(
                                            it, "cellphone-verify"
                                        ), StringArgumentType.getString(it, "captcha")
                                    );1
                                })
                            )
                        )
                    )
                ).then(
                    CommandManager.literal("email").then(
                        CommandManager.argument("email-address", StringArgumentType.string())
                            .then(CommandManager.argument("email-password", StringArgumentType.string()).executes {
                                    this.executeEmailLogin(
                                        it, StringArgumentType.getString(
                                            it, "email-address"
                                        ), StringArgumentType.getString(it, "email-password")
                                    );1
                                })
                    )
                )
            ).then(CommandManager.literal("logout").requires { it.hasPermissionLevel(2) }
                .executes { this.executeLogout(it);1 }).then(CommandManager.literal("search").then(
                CommandManager.argument("keyword", StringArgumentType.string()).then(
                    CommandManager.literal("limit")
                        .then(CommandManager.argument("limit", IntegerArgumentType.integer(2)).executes {
                            this.executeSearch(
                                it, StringArgumentType.getString(
                                    it, "keyword"
                                ), IntegerArgumentType.getInteger(it, "limit")
                            );1
                        })
                )
            ).executes { this.executeSearch(it, StringArgumentType.getString(it, "keyword"), 10);1 })
        )
        dispatcher.register(CommandManager.literal("rm").redirect(node))
    }

    fun executePlay(ctx: CommandContext<ServerCommandSource>, songId: String)

    fun executeStop(ctx: CommandContext<ServerCommandSource>)

    fun executeMute(ctx: CommandContext<ServerCommandSource>)

    fun executePause(ctx: CommandContext<ServerCommandSource>)

    fun executeResume(ctx: CommandContext<ServerCommandSource>)

    fun executeEmailLogin(ctx: CommandContext<ServerCommandSource>, email: String, password: String)

    fun executePhoneLogin(ctx: CommandContext<ServerCommandSource>, cellphone: String, password: String)

    fun executeSendCaptcha(ctx: CommandContext<ServerCommandSource>, cellphone: String)

    fun executeVerifyCaptcha(ctx: CommandContext<ServerCommandSource>, cellphone: String, captcha: String)

    fun executeLogout(ctx: CommandContext<ServerCommandSource>)

    fun executeSearch(ctx: CommandContext<ServerCommandSource>, keyword: String, limit: Int)

}