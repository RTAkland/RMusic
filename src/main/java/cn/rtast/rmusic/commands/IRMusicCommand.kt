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
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

interface IRMusicCommand {

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val root = dispatcher.register(
            CommandManager.literal("rmusic").then(
                CommandManager.literal("play").then(
                    CommandManager.argument("song_id", LongArgumentType.longArg()).executes {
                        this.executePlay(it, LongArgumentType.getLong(it, "song_id"));1
                    }
                )
            ).then(
                CommandManager.literal("pause").executes { this.executePause(it);1 }
            ).then(
                CommandManager.literal("resume").executes { this.executeResume(it);1 }
            ).then(
                CommandManager.literal("stop").executes { this.executeStop(it);1 }
            ).then(
                CommandManager.literal("volume").then(
                    CommandManager.argument("volume", IntegerArgumentType.integer(0, 100)).executes {
                        this.executeVolume(it, IntegerArgumentType.getInteger(it, "volume"));1
                    }
                )
            ).then(
                CommandManager.literal("search").then(
                    CommandManager.argument("keyword", StringArgumentType.string()).executes {
                        this.executeSearch(it, StringArgumentType.getString(it, "keyword"));1
                    }
                )
            ).then(
                CommandManager.literal("login").requires {
                    it.hasPermissionLevel(2)
                }.then(
                    CommandManager.literal("email").then(
                        CommandManager.argument("email_address", StringArgumentType.string()).then(
                            CommandManager.argument("email_password", StringArgumentType.string()).executes {
                                this.executeLoginEMailPwd(
                                    it, StringArgumentType.getString(it, "email_address"),
                                    StringArgumentType.getString(it, "email_password")
                                );1
                            }
                        )
                    )
                ).then(
                    CommandManager.literal("phone").then(
                        CommandManager.literal("captcha").then(
                            CommandManager.literal("send").then(
                                CommandManager.argument("phone", LongArgumentType.longArg()).executes {
                                    this.executeSendCaptcha(it, LongArgumentType.getLong(it, "phone"));1
                                }
                            )
                        ).then(
                            CommandManager.literal("verify").then(
                                CommandManager.argument("phone", LongArgumentType.longArg()).then(
                                    CommandManager.argument("captcha", StringArgumentType.string()).executes {
                                        this.executeVerifyCaptcha(
                                            it, LongArgumentType.getLong(it, "phone"),
                                            StringArgumentType.getString(it, "captcha")
                                        );1
                                    }
                                )
                            )
                        )
                    ).then(
                        CommandManager.argument("phone", LongArgumentType.longArg()).then(
                            CommandManager.argument("phone_password", StringArgumentType.string()).executes {
                                this.executeLoginPhonePwd(
                                    it, LongArgumentType.getLong(it, "phone"),
                                    StringArgumentType.getString(it, "phone_password")
                                );1
                            }
                        )
                    )
                )
            ).then(
                CommandManager.literal("logout").requires {
                    it.hasPermissionLevel(2)
                }.executes { this.executeLogout(it);1 }
            )
        )
        dispatcher.register(CommandManager.literal("rm").redirect(root))
    }


    fun executePlay(source: CommandContext<ServerCommandSource>, songId: Long)

    fun executePause(source: CommandContext<ServerCommandSource>)

    fun executeResume(source: CommandContext<ServerCommandSource>)

    fun executeStop(source: CommandContext<ServerCommandSource>)

    fun executeVolume(source: CommandContext<ServerCommandSource>, volume: Int)

    fun executeSearch(source: CommandContext<ServerCommandSource>, keyword: String)

    fun executeSendCaptcha(source: CommandContext<ServerCommandSource>, phone: Long)

    fun executeVerifyCaptcha(source: CommandContext<ServerCommandSource>, phone: Long, captcha: String)

    fun executeLoginPhonePwd(source: CommandContext<ServerCommandSource>, phone: Long, password: String)

    fun executeLoginEMailPwd(source: CommandContext<ServerCommandSource>, email: String, password: String)

    fun executeLogout(source: CommandContext<ServerCommandSource>)
}