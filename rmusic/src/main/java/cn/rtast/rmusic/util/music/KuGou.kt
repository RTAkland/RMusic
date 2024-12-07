/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/7
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.util.Http

object KuGou {

    private inline fun <reified T> get(endpoint: String, params: MutableMap<String, Any>? = null): T? {
        return Http.get<T>(endpoint, params.apply { put("cookie" to ) })
    }

    fun search(keyword: String) {

    }

}