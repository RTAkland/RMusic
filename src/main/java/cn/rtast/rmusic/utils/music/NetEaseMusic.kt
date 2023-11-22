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


package cn.rtast.rmusic.utils.music

import cn.rtast.rmusic.*
import cn.rtast.rmusic.entities.CaptchaSendEntity
import cn.rtast.rmusic.entities.LoginResponseEntity
import cn.rtast.rmusic.entities.search.SearchEntity
import cn.rtast.rmusic.entities.url.SongUrlEntity
import cn.rtast.rmusic.models.CaptchaModel
import cn.rtast.rmusic.utils.fromJson
import cn.rtast.rmusic.utils.http.HttpManager
import cn.rtast.rmusic.utils.http.Params

class NetEaseMusic {

    fun search(keyword: String, limit: Int): SearchEntity {
        val params = Params.Builder().addParam("keywords", keyword).addParam("limit", limit).build()
        val resp = HttpManager.get(API_ROOT_PATH + SEARCH_PATH, params, null).body.string()
        return resp.fromJson<SearchEntity>()
    }

    fun loginCellphonePwd(cellphone: String, password: String): String? {
        val params = Params.Builder().addParam("phone", cellphone).addParam("password", password).build()
        val resp = HttpManager.get(API_ROOT_PATH + CELLPHONE_LOGIN_PATH, params, null).body.string()
        val json = resp.fromJson<LoginResponseEntity>()
        return if (json.code == 200) json.cookie else null
    }

    fun loginEmailPwd(email: String, password: String): String? {
        val params = Params.Builder().addParam("email", email).addParam("password", password).build()
        val resp = HttpManager.get(API_ROOT_PATH + EMAIL_LOGIN_PATH, params, null).body.string()
        val json = resp.fromJson<LoginResponseEntity>()
        return if (json.code == 200) json.cookie else null
    }

    fun loginCellphoneCaptcha(cellphone: String, captcha: String): String? {
        val params = Params.Builder().addParam("phone", cellphone).addParam("captcha", captcha).build()
        val resp = HttpManager.get(API_ROOT_PATH + VERIFY_CAPTCHA_PATH, params, null).body.string()
        val json = resp.fromJson<LoginResponseEntity>()
        return if (json.code == 200) json.cookie else null
    }

    fun sendCaptcha(cellphone: String): CaptchaModel {
        val params = Params.Builder().addParam("phone", cellphone).build()
        val resp = HttpManager.get(API_ROOT_PATH + CAPTCHA_SEND_PATH, params, null).body.string()
        val json = resp.fromJson<CaptchaSendEntity>()
        return if (json.code == 200) CaptchaModel(json.code, "二维码已发送") else CaptchaModel(json.code, json.message)
    }

    fun getSongUrl(songId: String): String? {
        val params = Params.Builder().addParam("id", songId).addParam("br", 320000).build()
        val resp = HttpManager.get(API_ROOT_PATH + SONG_URL_V1_PATH, params, null).body.string()
        val json = resp.fromJson<SongUrlEntity>()
        return if (json.code == 200) json.data.first().url else null
    }
}