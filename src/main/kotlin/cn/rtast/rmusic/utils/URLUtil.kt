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

import java.net.URLDecoder
import java.net.URLEncoder

object URLUtil {
    fun encodeURI(uri: String): String {
        return URLEncoder.encode(uri, "UTF-8")
    }

    fun decodeURI(uri: String): String {
        return URLDecoder.decode(uri, "UTF-8")
    }
}