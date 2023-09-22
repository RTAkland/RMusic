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

class RMusicCommand : BaseCommand {

    override fun executePlay(source: CommandContext<ServerCommandSource>, songId: String) {
        println("a")
    }

    override fun executeStop(source: CommandContext<ServerCommandSource>) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executePause(source: CommandContext<ServerCommandSource>) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeResume(source: CommandContext<ServerCommandSource>) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeVolume(source: CommandContext<ServerCommandSource>, value: Double) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeLoginWithEmail(source: CommandContext<ServerCommandSource>, email: String, passwd: String) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeLoginWithQRCode(source: CommandContext<ServerCommandSource>) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeLoginWithCellphone(
        source: CommandContext<ServerCommandSource>,
        cellphone: Long,
        passwd: String?
    ) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeLogout(source: CommandContext<ServerCommandSource>) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeVerifyQRCode(source: CommandContext<ServerCommandSource>, key: String) {
        if (source.source.isExecutedByPlayer) {

        } else {

        }
    }

    override fun executeVerifyCaptcha(source: CommandContext<ServerCommandSource>, cellphone: Long, captcha: String) {
        TODO("Not yet implemented")
    }

    override fun executeSetAPIHost(source: CommandContext<ServerCommandSource>, newHost: String) {

    }
}