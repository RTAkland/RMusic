/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.player

import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.io.File
import java.net.URL

class MusicPlayer : StreamPlayer(), StreamPlayerListener {

    fun play(url: URL) {
        open(url)
        play()
    }

    fun play(file: File) {
        open(file)
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
        val totalBytes = totalBytes
        val progress =
            if (nEncodedBytes > 0 && totalBytes > 0) (nEncodedBytes * 1.0f / totalBytes * 1.0f).toDouble() else (-1.0f).toDouble()
        println("Seconds  : " + (microsecondPosition / 1000000).toInt() + " s " + "Progress: [ " + progress * 100 + " ] %")
    }

    override fun statusUpdated(event: StreamPlayerEvent?) {}
}