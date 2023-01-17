/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.player

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.client.RMusicClient
import cn.rtast.rmusic.models.netease.lyric.ext.Lyric
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.net.URL

class MusicPlayer : StreamPlayer(), StreamPlayerListener {

    var lyric: List<Lyric>? = null

    init {
        addStreamPlayerListener(this)
    }

    fun play(url: String) {
        open(URL(url))
        play()
    }

    override fun opened(dataSource: Any, properties: MutableMap<String, Any>) {
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray,
        properties: MutableMap<String, Any>
    ) {
        val currentSecond = (microsecondPosition / 1000000).toInt()
        lyric?.forEach {
            if (it.tp == currentSecond -1) {
                println()
                RMusic.CLIENT.player?.sendMessage(
                    Text.literal(it.ly)
                        .styled { s -> s.withColor(Formatting.RED) }, true
                )
            }
        }
    }

    override fun statusUpdated(event: StreamPlayerEvent) {
        if (event.playerStatus!! == Status.STOPPED) {
            RMusicClient.player = null
        }
    }
}