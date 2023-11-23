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

import cn.rtast.rmusic.enums.Actions
import cn.rtast.rmusic.models.PayloadModel
import cn.rtast.rmusic.utils.music.NetEaseMusic
import cn.rtast.rmusic.utils.sendPacket
import cn.rtast.rmusic.utils.toJson
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource


class RMusicCommand : IRMusicCommand {

    override fun executePlay(ctx: CommandContext<ServerCommandSource>, songId: String) {
        val payload = PayloadModel(Actions.Play, songId)
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executeStop(ctx: CommandContext<ServerCommandSource>) {
        val payload = PayloadModel(Actions.Stop, null)
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executeMute(ctx: CommandContext<ServerCommandSource>) {
        val payload = PayloadModel(Actions.Mute, null)
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executePause(ctx: CommandContext<ServerCommandSource>) {
        val payload = PayloadModel(Actions.Pause, null)
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executeResume(ctx: CommandContext<ServerCommandSource>) {
        val payload = PayloadModel(Actions.Resume, null)
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executeEmailLogin(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        val payload = PayloadModel(Actions.LoginEmailPwd, "$email|$password")
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executePhoneLogin(ctx: CommandContext<ServerCommandSource>, cellphone: String, password: String) {
        val payload = PayloadModel(Actions.LoginPhonePwd, "$cellphone|$password")
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executeLogout(ctx: CommandContext<ServerCommandSource>) {
        val payload = PayloadModel(Actions.Logout, null)
        sendPacket(payload, ctx.source.player!!)
    }

    override fun executeSearch(ctx: CommandContext<ServerCommandSource>, keyword: String, limit: Int) {
        val result = NetEaseMusic().search(keyword, limit)
        val payload = PayloadModel(Actions.Search, result.toJson())
        sendPacket(payload, ctx.source.player!!)
    }
}