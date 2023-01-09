package cn.rtast.rmusic.utils

import cn.rtast.rmusic.music.NeteaseMusic
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.net.URL

class MusicPlayer : StreamPlayer(), StreamPlayerListener {
    init {
        addStreamPlayerListener(this)
    }

    fun playMusic(id: String, platform: String) {
        val url = NeteaseMusic().getMusicURL(id).data[0].url
        open(URL(url))
        play()
    }

    fun playMusic(url: String) {
        open(URL(url))
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
    }
}