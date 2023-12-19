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

import cn.rtast.rmusic.NetEaseMusicAPI
import cn.rtast.rmusic.entities.login.CommonLoginEntity
import cn.rtast.rmusic.entities.login.qrcode.CheckQRCodeStatusEntity
import cn.rtast.rmusic.entities.login.qrcode.GenKeyEntity
import cn.rtast.rmusic.entities.login.qrcode.GenQRCodeEntity
import cn.rtast.rmusic.entities.lyric.LyricEntity
import cn.rtast.rmusic.entities.search.SearchEntity
import cn.rtast.rmusic.entities.song.SongEntity
import cn.rtast.rmusic.models.LoginModel
import cn.rtast.rmusic.models.QRCodeModel
import cn.rtast.rmusic.models.SongModel
import cn.rtast.rmusic.utils.str.JsonUtil

class NetEaseMusic {

    fun search(keyword: String): MutableList<SongModel> {
        val response = Http.get(
            "$NetEaseMusicAPI/cloudsearch", params = mapOf(
                "keywords" to keyword, "limit" to "10"
            )
        )

        val json = JsonUtil.fromJson<SearchEntity>(response)
        val searchResult = mutableListOf<SongModel>()
        json.result.songs.forEach { song ->
            val artistsString = StringBuilder()
            val artists = mutableListOf<String>()
            song.ar.forEach {
                artistsString.append(it.name + ",")
                artists.add(it.name)
            }
            artistsString.removeSuffix(",")
            searchResult.add(SongModel(song.id, song.name, artists, artistsString.toString()))
        }
        return searchResult
    }

    fun getSongUrl(id: Long): String {
        val response = Http.get(
            "$NetEaseMusicAPI/song/url/v1", params = mapOf(
                "id" to id.toString(), "level" to "standard"
            )
        )

        return JsonUtil.fromJson<SongEntity>(response).data.first().url
    }

    fun getLyric(id: Long): String {
        val response = Http.get(
            "$NetEaseMusicAPI/lyric", params = mapOf(
                "id" to id.toString()
            )
        )

        return JsonUtil.fromJson<LyricEntity>(response).lrc.lyric
    }

    fun generateKey(): String {
        val response = Http.get("$NetEaseMusicAPI/login/qr/key", null)
        return JsonUtil.fromJson<GenKeyEntity>(response).data.unikey
    }

    fun checkQRCodeStatus(key: String): LoginModel {
        val response = Http.get("$NetEaseMusicAPI/login/qr/check", params = mapOf("key" to key))
        val json = JsonUtil.fromJson<CheckQRCodeStatusEntity>(response)
        return when (json.code) {
            800 -> {
                LoginModel(null, false)
            }

            801 -> {
                LoginModel(null, false)
            }

            802 -> {
                LoginModel(null, false)
            }

            else -> {
                LoginModel(json.cookie, true)
            }
        }
    }

    fun generateQRCode(key: String): QRCodeModel {
        val response = Http.get(
            "$NetEaseMusicAPI/login/qr/create",
            params = mapOf("key" to key, "qrimg" to "1")
        )
        val json = JsonUtil.fromJson<GenQRCodeEntity>(response)
        return QRCodeModel(json.data.qrurl, json.data.qrimg)
    }

    fun sendCaptcha(phone: Long) {
        Http.get("$NetEaseMusicAPI/captcha/sent", params = mapOf("phone" to phone.toString()))
    }

    fun verifyCaptcha(phone: Long, captcha: String): LoginModel {
        val response = Http.get(
            "$NetEaseMusicAPI/login/cellphone",
            params = mapOf("phone" to phone.toString(), "captcha" to captcha)
        )

        val json = JsonUtil.fromJson<CommonLoginEntity>(response)
        return if (json.code == 200) {
            LoginModel(json.cookie, true)
        } else {
            LoginModel(null, false)
        }
    }

    fun phoneAuth(phone: Long, password: String): LoginModel {
        val response = Http.get(
            "$NetEaseMusicAPI/login/cellphone",
            params = mapOf("phone" to phone.toString(), "password" to password)
        )
        val json = JsonUtil.fromJson<CommonLoginEntity>(response)
        return if (json.code == 200) {
            LoginModel(json.cookie, true)
        } else {
            LoginModel(null, false)
        }
    }

    fun emailAuth(email: String, password: String): LoginModel {
        val response = Http.get(
            "$NetEaseMusicAPI/login",
            params = mapOf("email" to email, "password" to password)
        )
        val json = JsonUtil.fromJson<CommonLoginEntity>(response)
        return if (json.code == 200) {
            LoginModel(json.cookie, true)
        } else {
            LoginModel(null, false)
        }
    }

}