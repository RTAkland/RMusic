/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.player

import cn.rtast.rmusic.client.RMusicClient
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.net.URL

class MusicPlayer : StreamPlayer(), StreamPlayerListener {

    init {
        addStreamPlayerListener(this)
    }

    fun play(url: URL) {
        open(url)
        play()
    }

    override fun opened(dataSource: Any?, properties: MutableMap<String, Any>?) {
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray?,
        properties: MutableMap<String, Any>?
    ) {
    }

    override fun statusUpdated(event: StreamPlayerEvent?) {
        if (event?.playerStatus!! == Status.STOPPED) {
            RMusicClient.player = null
        }
    }
}