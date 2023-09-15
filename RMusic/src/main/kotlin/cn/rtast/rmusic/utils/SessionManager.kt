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

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.exceptions.SessionException
import cn.rtast.rmusic.models.ConfigModel

class SessionManager {

    private val conf = "/conf.json".openFile()

    init {
        if (!conf.exists()) {
            conf.writeText(ConfigModel(RMusic.API_HOST, null, false).toJson())
        }
    }

    private fun readConfJson(): ConfigModel {
        return this.conf.readText().fromJson<ConfigModel>()
    }

    private fun writeConfJson(conf: ConfigModel) {
        this.conf.writeText(conf.toJson())
    }


    fun getStatus(): SessionStatus {
        return if (this.readConfJson().sessionStatus) SessionStatus.LoggedIn else SessionStatus.Anonymous
    }

    fun setStatus(status: SessionStatus) {
        val confJson = this.readConfJson()
        confJson.sessionStatus = (status == SessionStatus.LoggedIn)
    }

    fun getApiHost(): String {
        return this.readConfJson().apiHost
    }

    fun setApiHost(host: String) {
        val confJson = this.readConfJson()
        confJson.apiHost = host
        this.writeConfJson(confJson)
    }

    fun getCookie(): String {
        if (this.getStatus() == SessionStatus.Anonymous) throw SessionException("You are not logged in yet!")
        return this.readConfJson().cookie!!
    }

    enum class SessionStatus {
        LoggedIn, Anonymous
    }
}