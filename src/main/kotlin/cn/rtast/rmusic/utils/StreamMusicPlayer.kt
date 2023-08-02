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

import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.net.URL
import kotlin.math.log10

class StreamMusicPlayer : StreamPlayer(), StreamPlayerListener {

    init {
        addStreamPlayerListener(this)
    }

    fun playMusic(url: String) {
        if (this.isPlaying) {
            this.stop()
        }
        this.open(URL(url))
        this.play()
    }

    fun setVolume(numerical: Double) {
        this.setGain(log10(50.0) / 20)
    }


    override fun opened(dataSource: Any?, properties: MutableMap<String, Any>?) {
        this.setGain(log10(50.0) / 20)
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray?,
        properties: MutableMap<String, Any>?
    ) {

    }

    override fun statusUpdated(event: StreamPlayerEvent?) {

    }
}