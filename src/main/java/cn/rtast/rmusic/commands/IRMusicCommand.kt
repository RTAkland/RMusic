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
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

interface IRMusicCommand : CommandRegistrationCallback {
    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        val node = dispatcher.register(CommandManager.literal("rmusic").then(CommandManager.literal("play")
                .then(CommandManager.argument("song-id", StringArgumentType.string())
                    .executes { this.executePlay(StringArgumentType.getString(it, "song-id")) }))
            .then(CommandManager.literal("stop").executes { this.executeStop() })
            .then(CommandManager.literal("pause").executes { this.executePause() })
            .then(CommandManager.literal("resume").executes { this.executeResume() })
            .then(CommandManager.literal("mute").executes { this.executeMute() }).then(CommandManager.literal("login")
                .then(CommandManager.literal("cellphone")
                    .then(CommandManager.argument("cellphone-number", StringArgumentType.string())
                        .then(CommandManager.argument("cellphone-password", StringArgumentType.string()).executes {
                                this.executePhoneLogin(
                                    StringArgumentType.getString(
                                        it, "cellphone-number"
                                    ), StringArgumentType.getString(it, "cellphone-password")
                                )
                            })).then(CommandManager.literal("captcha")
                        .then(CommandManager.literal("send").then(CommandManager.argument(
                                "cellphone-captcha", StringArgumentType.string()
                            ).executes {
                                    this.executeSendCaptcha(
                                        StringArgumentType.getString(
                                            it, "cellphone-captcha"
                                        )
                                    )
                                })).then(CommandManager.literal("verify").then(CommandManager.argument(
                                "cellphone-verify", StringArgumentType.string()
                            ).then(CommandManager.argument(
                                    "captcha", StringArgumentType.string()
                                ).executes {
                                        this.executeVerifyCaptcha(
                                            StringArgumentType.getString(
                                                it, "cellphone-verify"
                                            ), StringArgumentType.getString(it, "captcha")
                                        )
                                    }))))).then(CommandManager.literal("email")
                    .then(CommandManager.argument("email-address", StringArgumentType.string())
                        .then(CommandManager.argument("email-password", StringArgumentType.string()).executes {
                                this.executeEmailLogin(
                                    StringArgumentType.getString(
                                        it, "email-address"
                                    ), StringArgumentType.getString(it, "email-password")
                                );1
                            })))).then(CommandManager.literal("logout").executes { this.executeLogout() })
            .then(CommandManager.literal("search").then(CommandManager.argument("keyword", StringArgumentType.string())
                    .then(CommandManager.literal("limit")
                        .then(CommandManager.argument("limit", IntegerArgumentType.integer(2)).executes {
                                this.executeSearch(
                                    StringArgumentType.getString(
                                        it, "keyword"
                                    ), IntegerArgumentType.getInteger(it, "limit")
                                )
                            }))).executes { this.executeSearch(StringArgumentType.getString(it, "keyword"), 10) }))
        dispatcher.register(CommandManager.literal("rm").redirect(node))
    }

    fun executePlay(songId: String): Int

    fun executeStop(): Int

    fun executeMute(): Int

    fun executePause(): Int

    fun executeResume(): Int

    fun executeEmailLogin(email: String, password: String): Int

    fun executePhoneLogin(cellphone: String, password: String): Int

    fun executeSendCaptcha(cellphone: String): Int

    fun executeVerifyCaptcha(cellphone: String, captcha: String): Int

    fun executeLogout(): Int

    fun executeSearch(keyword: String, limit: Int): Int

}