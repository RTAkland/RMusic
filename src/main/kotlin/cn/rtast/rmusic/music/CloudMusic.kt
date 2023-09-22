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

package cn.rtast.rmusic.music

import cn.rtast.rmusic.entities.*
import cn.rtast.rmusic.exceptions.CaptchaException
import cn.rtast.rmusic.models.Login
import cn.rtast.rmusic.models.QRCodeImage
import cn.rtast.rmusic.models.Song
import cn.rtast.rmusic.utils.Http
import cn.rtast.rmusic.utils.currentTime
import cn.rtast.rmusic.utils.fromJson
import cn.rtast.rmusic.utils.removeLast2

class CloudMusic {

    fun getQRCodeKey(): String {
        return Http.get("/login/qr/key").body.string().fromJson<QRKey>().data.unikey
    }

    fun getQRCodeImage(key: String): QRCodeImage {
        val resp = Http.get(
            "/login/qr/create?qrimg=true", mapOf("key" to key)
        ).body.string().fromJson<QRCodeImageEntity>()
        return QRCodeImage(resp.data.qrurl, resp.data.qrimg, key)
    }

    fun checkQRCode(key: String): String? {
        val resp = Http.get(
            "/login/qr/check", mapOf("key" to key)
        ).body.string().fromJson<QRCodeCheckModel>()

        return if (resp.code != 200) null else resp.cookie
    }

    fun searchMusic(keyword: String, limit: Int): List<Song> {
        val resp = Http.get(
            "/search", mapOf("keywords" to keyword, "limit" to limit.toString())
        ).body.string().fromJson<SearchEntity>().result.songs

        val songs = mutableListOf<Song>()
        resp.forEach {
            val artists = StringBuilder()
            it.artists.forEach { art ->
                artists.append("${art.name} | ")
            }
            songs.add(Song(it.name, it.id, artists.toString().removeLast2()))
        }
        return songs

    }

    fun getSongUrl(id: Long): String {
        return Http.get(
            "/song/url", mapOf("id" to id.toString())
        ).body.string().fromJson<SongUrlEntity>().data.first().url

    }

    fun getLyric(id: Long): LyricEntity {
        return Http.get("/lyric", mapOf("id" to id.toString())).body.string().fromJson<LyricEntity>()
    }

    fun sendCaptcha(cellphone: Long) {
        val resp = Http.get(
            "/captcha/sent", mapOf("phone" to cellphone.toString())
        ).body.string().fromJson<CaptchaEntity>()
        if (resp.code != 200) {
            throw CaptchaException("The phone number is invalid!")
        }
    }

    fun verifyCaptcha(cellphone: Long, captcha: String): Boolean {
        val resp = Http.get(
            "/captcha/verify", mapOf(
                "phone" to cellphone.toString(), "captcha" to captcha, "timestamp" to currentTime().toString()
            )
        ).body.string().fromJson<CaptchaEntity>()
        return resp.code == 200

    }

    fun loginWithEmail(email: String, password: String): Login {
        val resp = Http.get(
            "/login", mapOf(
                "email" to email, "password" to password
            )
        ).body.string().fromJson<LoginEntity>()
        return if (resp.code != 200) {
            Login(resp.code, resp.profile, null)
        } else {
            Login(200, resp.profile, resp.cookie)
        }
    }

    fun loginWithPhoneCaptcha(cellphone: Long, captcha: String): Login {
        val resp = Http.get(
            "/login/cellphone", mapOf(
                "phone" to cellphone.toString(), "captcha" to captcha
            )
        ).body.string().fromJson<LoginEntity>()
        return if (resp.code != 200) {
            Login(resp.code, null, null)
        } else {
            Login(200, resp.profile, resp.cookie)
        }
    }

    fun loginWithPhone(cellphone: Long, password: String): Login {
        val resp = Http.get(
            "/login/cellphone", mapOf(
                "phone" to cellphone.toString(), "password" to password
            )
        ).body.string().fromJson<LoginEntity>()
        return if (resp.code != 200) {
            Login(resp.code, null, null)
        } else {
            Login(200, resp.profile, resp.cookie)
        }
    }
}