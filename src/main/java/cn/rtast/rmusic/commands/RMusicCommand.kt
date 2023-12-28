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

import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

class RMusicCommand: IRMusicCommand {

    override fun executePlay(source: CommandContext<ServerCommandSource>, songId: Long) {
        if (source.source.world.isClient) {

        }
    }

    override fun executePause(source: CommandContext<ServerCommandSource>) {
        if (source.source.world.isClient) {

        }
    }

    override fun executeResume(source: CommandContext<ServerCommandSource>) {
        if (source.source.world.isClient) {

        }
    }

    override fun executeStop(source: CommandContext<ServerCommandSource>) {
        if (source.source.world.isClient) {

        }
    }

    override fun executeVolume(source: CommandContext<ServerCommandSource>, volume: Int) {
        if (source.source.world.isClient) {

        }
    }

    override fun executeSearch(source: CommandContext<ServerCommandSource>, keyword: String) {
        if (source.source.world.isClient) {

        }
    }

    override fun executeSendCaptcha(source: CommandContext<ServerCommandSource>, phone: Long) {

    }

    override fun executeVerifyCaptcha(source: CommandContext<ServerCommandSource>, phone: Long, captcha: String) {

    }

    override fun executeLoginPhonePwd(source: CommandContext<ServerCommandSource>, phone: Long, password: String) {

    }

    override fun executeLoginEMailPwd(source: CommandContext<ServerCommandSource>, email: String, password: String) {

    }

    override fun executeLogout(source: CommandContext<ServerCommandSource>) {

    }
}