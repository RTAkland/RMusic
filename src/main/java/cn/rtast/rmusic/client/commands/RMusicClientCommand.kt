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
import cn.rtast.rmusic.commands.RMusicCommand

class RMusicClientCommand: IRMusicCommand {
    override fun executePlay(songId: String): Int {
        TODO("Not yet implemented")
    }

    override fun executeStop(): Int {
        TODO("Not yet implemented")
    }

    override fun executeMute(): Int {
        TODO("Not yet implemented")
    }

    override fun executePause(): Int {
        TODO("Not yet implemented")
    }

    override fun executeResume(): Int {
        TODO("Not yet implemented")
    }

    override fun executeEmailLogin(email: String, password: String): Int {
        TODO("Not yet implemented")
    }

    override fun executePhoneLogin(cellphone: String, password: String): Int {
        TODO("Not yet implemented")
    }

    override fun executeSendCaptcha(cellphone: String): Int {
        TODO("Not yet implemented")
    }

    override fun executeVerifyCaptcha(cellphone: String, captcha: String): Int {
        TODO("Not yet implemented")
    }

    override fun executeLogout(): Int {
        TODO("Not yet implemented")
    }

    override fun executeSearch(keyword: String, limit: Int): Int {
        TODO("Not yet implemented")
    }
}