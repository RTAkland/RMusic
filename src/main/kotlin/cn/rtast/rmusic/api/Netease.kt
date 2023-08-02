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

package cn.rtast.rmusic.api

import cn.rtast.rmusic.RMusic.Companion.API_HOST
import cn.rtast.rmusic.entities.login.qr.key.QRIMGBean
import cn.rtast.rmusic.entities.login.qr.state.QRCodeStateBean
import cn.rtast.rmusic.entities.search.SearchBean
import cn.rtast.rmusic.entities.url.SongUrlBean
import cn.rtast.rmusic.enums.QRCodeState
import cn.rtast.rmusic.models.search.SearchData
import cn.rtast.rmusic.models.search.SearchModel
import cn.rtast.rmusic.utils.CookieUtil
import cn.rtast.rmusic.utils.HttpUtil
import cn.rtast.rmusic.utils.JsonUtil

class Netease {

    fun loginByQRCode(): String {
        val qrKey = HttpUtil.get("$API_HOST/login/qr/key")
        val qrcode = HttpUtil.get("$API_HOST/login/qr/create", mapOf("key" to qrKey, "qrimg" to "1"))
        val base64 = JsonUtil.fromJson<QRIMGBean>(qrcode)
        return base64.data.qrimg
    }

    fun logout(): Boolean {
        return CookieUtil.removeCookie()
    }

    fun checkQRCodeState(key: String): QRCodeState {
        val state = HttpUtil.get("$API_HOST/login/qr/state", mapOf("key" to key))
        val code = JsonUtil.fromJson<QRCodeStateBean>(state)
        return when (code.code) {
            800 -> QRCodeState.QRCodeExpired
            801 -> QRCodeState.WaitingForScanning
            802 -> QRCodeState.WaitingForConfirm
            else -> QRCodeState.AuthorizedSuccessfully
        }
    }

    fun search(keyword: String): SearchModel {
        val searchResult = HttpUtil.get("$API_HOST/search", mapOf("keywords" to keyword, "limit" to "10"))
        val result = JsonUtil.fromJson<SearchBean>(searchResult)
        val searchBeanList = mutableListOf<SearchData>()
        result.result.songs.forEach {
            val artistsList = mutableListOf<String>()
            it.artists.forEach { art ->
                artistsList.add(art.name)
            }
            val songId = it.id
            val songName = it.name
            searchBeanList.add(SearchData(artistsList, songName, songId))
        }
        return SearchModel(searchBeanList)
    }

    fun getSongUrl(id: Long): String {
        val result = HttpUtil.get("$API_HOST/song/url", mapOf("id" to id.toString()))
        val json = JsonUtil.fromJson<SongUrlBean>(result)
        return json.data[0].url
    }
}