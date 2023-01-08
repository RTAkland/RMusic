package cn.rtast.rmusic.player

import cn.rtast.rmusic.music.Music163
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.net.URL

class CommonPlayer : StreamPlayer(), StreamPlayerListener {

    fun playMusic(id: Int) {
        val info = Music163().getSongUrl(id)
        open(URL(info.url))
        play()
    }

    override fun opened(dataSource: Any?, properties: MutableMap<String, Any>?) {
        println("Stream opened...")
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray?,
        properties: MutableMap<String, Any>?
    ) {
    }

    override fun statusUpdated(event: StreamPlayerEvent?) {}
}