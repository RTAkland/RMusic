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
import cn.rtast.rmusic.exceptions.RequestFailedException
import cn.rtast.rmusic.models.QRCodeImage
import cn.rtast.rmusic.models.Song
import cn.rtast.rmusic.utils.Http
import cn.rtast.rmusic.utils.fromJson
import cn.rtast.rmusic.utils.mappingQRCodeAuth

class CloudMusic {

    private suspend fun getQRCodeKey(): String {
        return Http.get("/login/qr/key").body.string()
    }

    private suspend fun getQRCodeImage(key: String): QRCodeImage {
        try {
            val resp = Http.get(
                "/login/qr/create?qrimg=true", mapOf("key" to key)
            ).body.string().fromJson<QRCodeImageEntity>()
            return QRCodeImage(resp.data.qrurl, resp.data.qrimg, key)
        } catch (_: NullPointerException) {
            throw RequestFailedException("Request was failed! Maybe something wrong with API?")
        }
    }

    suspend fun checkStatus(key: String): String? {
        try {
            val resp = Http.get(
                "/login/qr/check", mapOf("key" to key)
            ).body.string().fromJson<QRCodeCheckModel>()
            val status = mappingQRCodeAuth(resp.code)
            return if (status == QRCodeCheckStatus.AuthSuccessfully) resp.cookie else null
        } catch (_: NullPointerException) {
            throw RequestFailedException("Request was failed! Maybe something wrong with API?")
        }
    }

    suspend fun searchMusic(keyword: String, limit: Int): List<Song> {
        try {
            val resp = Http.get(
                "/search", mapOf("keywords" to keyword, "limit" to limit.toString())
            ).body.string().fromJson<SearchEntity>().result.songs

            val artistsString = StringBuilder()
            val songs = mutableListOf<Song>()
            resp.forEach {
                val artists = StringBuilder()
                it.artists.forEach { art ->
                    artists.append("${art.name} | ")
                }
                artists.deleteRange(artistsString.length - 2, artistsString.length)
                songs.add(Song(it.name, it.id, artists.toString()))
            }
            return songs

        } catch (_: NullPointerException) {
            throw RequestFailedException("Request was failed! Maybe something wrong with API?")
        }
    }

    suspend fun getSongUrl(id: Long): String {
        try {
            return Http.get(
                "/song/url", mapOf("id" to id.toString())
            ).body.string().fromJson<SongUrlEntity>().data.url

        } catch (_: NullPointerException) {
            throw RequestFailedException("Request was failed! Maybe something wrong with API?")
        }
    }

    suspend fun getLyric(id: Long): LyricEntity {
        try {
            return Http.get("/lyric", mapOf("id" to id.toString())).body.string().fromJson<LyricEntity>()
        } catch (_: NullPointerException) {
            throw RequestFailedException("Request was failed! Maybe something wrong with API?")
        }
    }

    suspend fun sendCaptcha(cellphone: Long) {
        try {
            val resp = Http.get(
                "/captcha/sent",
                mapOf("phone" to cellphone.toString())
            ).body.string()
                .fromJson<CaptchaEntity>()
            if (resp.code != 200) {
                throw CaptchaException("The phone number is invalid!")
            }
        } catch (_: NullPointerException) {
            throw RequestFailedException("Request was failed! Maybe something wrong with API?")

        }
    }

    suspend fun verifyCaptcha(cellphone: Long, captcha: String): Boolean {
        try {
            val resp = Http.get(
                "/captcha/verify",
                mapOf("phone" to cellphone.toString(), "captcha" to captcha)
            ).body.string().fromJson<CaptchaEntity>()
            return resp.code == 200

        } catch (_: NullPointerException) {
            throw RequestFailedException("Request was failed! Maybe something wrong with API?")

        }
    }
}