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
import cn.rtast.rmusic.entities.CookieEntity
import cn.rtast.rmusic.entities.qrcode.KeyEntity
import cn.rtast.rmusic.entities.qrcode.QRImageEntity
import cn.rtast.rmusic.entities.search.SearchEntity
import cn.rtast.rmusic.models.QRCodeModel
import cn.rtast.rmusic.utils.fromJson
import cn.rtast.rmusic.utils.http.HttpManager
import cn.rtast.rmusic.utils.http.Params

class NetEaseMusic {

    private fun getQRCodeKey(): String {
        val resp = HttpManager.get(QRCODE_KEY_GEN_PATH, null, null).body.string()
        return resp.fromJson<KeyEntity>().data.unikey
    }

    fun createQRCode(): QRCodeModel {
        val key = this.getQRCodeKey()
        val params = Params.Builder()
            .addParam("key", key)
            .addParam("qrimg", "1")
            .build()
        val resp = HttpManager.get(QRCODE_GEN_PATH, params, null).body.string()
        val json = resp.fromJson<QRImageEntity>()
        return QRCodeModel(json.data.qrimg, key, json.data.qrurl)
    }

    fun checkQRCodeState(key: String): Boolean {
        val params = Params.Builder()
            .addParam("key", key)
            .build()
        val resp = HttpManager.get(QRCODE_CHECK_PATH, params, null).body.string()
        return resp.fromJson<CookieEntity>().code == 803
    }

    fun search(keyword: String, limit: Int): SearchEntity {
        val params = Params.Builder()
            .addParam("keywords", keyword)
            .addParam("limit", limit)
            .build()
        val resp = HttpManager.get(SEARCH_PATH, params, null).body.string()
        return resp.fromJson<SearchEntity>()
    }

    fun loginCellphonePwd(cellphone: String, password: String) {
        val params = Params.Builder()
            .addParam("phone", cellphone)
            .addParam("password", password)
            .build()
        val resp = HttpManager.get(CELLPHONE_LOGIN_PATH, params, null).body.string()
        val json = resp.fromJson<>()
    }

    fun loginEmailPwd(email: String, password: String) {
        val params = Params.Builder()
            .addParam("email", email)
            .addParam("password", password)
            .build()
        val resp = HttpManager.get(EMAIL_LOGIN_PATH, params, null).body.string()

    }

    fun loginCellphoneCaptcha(cellphone: String, captcha: String) {
        val params = Params.Builder()
            .addParam("phone", cellphone)
            .addParam("captcha", captcha)
            .build()

    }

    fun sendCaptcha(cellphone: String) {
        val params = Params.Builder()
            .addParam("phone", cellphone)
            .build()

    }
}