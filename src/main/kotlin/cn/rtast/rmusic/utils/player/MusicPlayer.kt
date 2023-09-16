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

package cn.rtast.rmusic.utils.player

import cn.rtast.rmusic.utils.CloudMusic
import cn.rtast.rmusic.utils.toMinutes
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.net.URL

class MusicPlayer : StreamPlayer(), StreamPlayerListener {


    init {
        this.addStreamPlayerListener(this)
    }

    fun play(songId: Long) {
        val result = CloudMusic().getSongUrl(songId)
        this.open(URL(result))
        this.play()
    }

    override fun opened(dataSource: Any, properties: MutableMap<String, Any>) {

    }

    override fun progress(
        nEncodedBytes: Int, microsecondPosition: Long, pcmData: ByteArray, properties: Map<String, Any>
    ) {
        val minute = (microsecondPosition / 1000000).toInt().toMinutes()
        Progress.onProgress(minute)
    }


    override fun statusUpdated(event: StreamPlayerEvent) {
        Status.onStatusChanged(event)
    }
}