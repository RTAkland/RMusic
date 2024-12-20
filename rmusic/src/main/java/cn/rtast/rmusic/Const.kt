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


package cn.rtast.rmusic

import cn.rtast.rmusic.entity.Config
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.minecraft.util.Identifier
import java.io.File

val scope = CoroutineScope(Dispatchers.IO)
val gson: Gson = GsonBuilder().disableHtmlEscaping().create()
val qrcodeId: Identifier = Identifier.of("rmusic", "qr.code")
val defaultCoverId: Identifier = Identifier.of("rmusic", "texture/default.png")
val networkingId: Identifier = Identifier.of("rmusic", "networking")
val cacheDir = File("./config/rmusic/cache").apply { mkdirs() }
val defaultConfig = Config(
    "https://ncm.rtast.cn",
    "http://127.0.0.1:6868",
    "https://kugou.rtast.cn"
)
