/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/8 20:44
 */

package cn.rtast.rmusic.utils

import okhttp3.OkHttpClient
import okhttp3.Request

class HTTPUtil {

    private val client = OkHttpClient()

    fun get(url: String): String {
        val request = Request.Builder().url(url).get().build()
        return client.newCall(request).execute().body.string()
    }
}