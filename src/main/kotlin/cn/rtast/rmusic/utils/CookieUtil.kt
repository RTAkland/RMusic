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

import cn.rtast.rmusic.RMusic.Companion.confFile
import cn.rtast.rmusic.models.conf.ConfigModel
import cn.rtast.rmusic.models.conf.Netease

object CookieUtil {

    private val confString = confFile.readText(Charsets.UTF_8)
    private val confJsonString = JsonUtil.fromJson<ConfigModel>(confString)

    fun loginStatus(): Boolean {
        return confJsonString.logged
    }

    fun loadCookie(): String {
        return confJsonString.netease.cookie
    }

    fun removeCookie(): Boolean {
        // check local file
        return if (!confJsonString.logged) {
            false
        } else {
            val jsonObject = ConfigModel(
                confJsonString.version,
                Netease(confJsonString.netease.nickname, confJsonString.netease.cookie),
                false
            )
            JsonUtil.writeJson(jsonObject)
            true
        }
    }
}