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

import cn.rtast.rmusic.RMusic
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.io.File
import java.net.URL

class MusicPlayer : StreamPlayer(), StreamPlayerListener {

    init {
        addStreamPlayerListener(this)
    }

    fun play(url: URL) {
        this.open(url)
        this.play()
    }

    fun play(file: File) {
        this.open(file)
        this.play()
    }

    override fun opened(dataSource: Any, properties: MutableMap<String, Any>) {
        RMusic.logger.info("Stream opened.")
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray,
        properties: MutableMap<String, Any>
    ) {
        RMusic.logger.info(microsecondPosition / 1_000_000)
    }

    override fun statusUpdated(event: StreamPlayerEvent) {

    }
}