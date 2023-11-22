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


package cn.rtast.rmusic.client.commands

import cn.rtast.rmusic.commands.IRMusicCommand
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

class RMusicClientCommand : IRMusicCommand {

    override fun executePlay(ctx: CommandContext<ServerCommandSource>, songId: String) {
        TODO("Not yet implemented")
    }

    override fun executeStop(ctx: CommandContext<ServerCommandSource>) {
        TODO("Not yet implemented")
    }

    override fun executeMute(ctx: CommandContext<ServerCommandSource>) {
        TODO("Not yet implemented")
    }

    override fun executePause(ctx: CommandContext<ServerCommandSource>) {
        TODO("Not yet implemented")
    }

    override fun executeResume(ctx: CommandContext<ServerCommandSource>) {
        TODO("Not yet implemented")
    }

    override fun executeEmailLogin(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun executePhoneLogin(ctx: CommandContext<ServerCommandSource>, cellphone: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun executeSendCaptcha(ctx: CommandContext<ServerCommandSource>, cellphone: String) {
        TODO("Not yet implemented")
    }

    override fun executeVerifyCaptcha(ctx: CommandContext<ServerCommandSource>, cellphone: String, captcha: String) {
        TODO("Not yet implemented")
    }

    override fun executeLogout(ctx: CommandContext<ServerCommandSource>) {
        TODO("Not yet implemented")
    }

    override fun executeSearch(ctx: CommandContext<ServerCommandSource>, keyword: String, limit: Int) {
        TODO("Not yet implemented")
    }
}