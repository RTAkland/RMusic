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
import cn.rtast.rmusic.enums.PlayerStatus
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.net.URL

class MusicPlayer : StreamPlayerListener, StreamPlayer() {

    private var lyric: Map<Int, String>? = null

    init {
        this.addStreamPlayerListener(this)
    }

    override fun opened(dataSource: Any, properties: MutableMap<String, Any>) {
        RMusic.logger.info("Opened")
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray,
        properties: MutableMap<String, Any>,
    ) {
        val currentSecond = (microsecondPosition / 1000000).toInt()

        for (key in lyric!!.keys) {
            if (currentSecond == key) {
                Message.m(lyric!![key]!!)
            }
        }
    }

    override fun statusUpdated(event: StreamPlayerEvent) {

    }

    fun status(): PlayerStatus {
        return when (this.status) {
            Status.STOPPED -> PlayerStatus.Stopped
            Status.PAUSED -> PlayerStatus.Paused
            else -> PlayerStatus.Playing
        }
    }

    fun playMusic(songUrl: String, lyric: Map<Int, String>) {
        this.lyric = lyric
        this.open(URL(songUrl))
        this.play()
    }
}