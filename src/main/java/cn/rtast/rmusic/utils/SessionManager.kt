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


package cn.rtast.rmusic.utils

import cn.rtast.rmusic.DEFAULT_CONF_PATH
import cn.rtast.rmusic.models.SessionModel
import java.io.File

object SessionManager {

    var loginState = false
    var cookie: String? = null
    private val confFile = File(DEFAULT_CONF_PATH)

    init {
        if (!confFile.exists()) {
            confFile.createNewFile()
        }
    }

    fun setSessionCookie(cookie: String) {
        this.loginState = true
        this.cookie = cookie
        val model = SessionModel(this.cookie, true)
        confFile.writeText(model.toJson())
    }

    fun removeSessionCookie() {
        this.loginState = false
        this.cookie = null
        val model = SessionModel(null, false)
        confFile.writeText(model.toJson())
    }
}