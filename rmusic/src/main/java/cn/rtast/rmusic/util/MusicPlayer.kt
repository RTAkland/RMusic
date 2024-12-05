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


package cn.rtast.rmusic.util

import cn.rtast.rmusic.RMusicClient
import cn.rtast.rmusic.cacheDir
import cn.rtast.rmusic.entity.ncm.SongDetail
import cn.rtast.rmusic.enums.LyricPosition
import cn.rtast.rmusic.util.music.NCMusic
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class MusicPlayer : StreamPlayerListener, StreamPlayer() {

    private var lyric: Map<Int, String>? = null
    private var currentSongDetail: SongDetail? = null
    private val minecraftClient: MinecraftClient = MinecraftClient.getInstance()

    init {
        this.addStreamPlayerListener(this)
    }

    override fun opened(dataSource: Any, properties: MutableMap<String, Any>) {
        if (RMusicClient.configManager.config?.position == LyricPosition.TopLeft) {
            Renderer.renderLyric()
        }
        minecraftClient.soundManager.stopAll()
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray,
        properties: MutableMap<String, Any>
    ) {
        setGain(minecraftClient.options.getSoundVolume(SoundCategory.RECORDS).toDouble())
        val currentSecond = (microsecondPosition / 1000000).toInt()
        for (key in lyric!!.keys) {
            if (currentSecond == key) {
                Renderer.loadCurrentLyric = lyric!![key] ?: continue
                val lyric = Text.literal(Renderer.loadCurrentLyric)
                    .styled { it.withColor(Formatting.YELLOW) }
                if (RMusicClient.configManager.config?.position == LyricPosition.ActionBar) {
                    minecraftClient.inGameHud.setOverlayMessage(lyric, false)
                }
            }
        }
    }

    override fun statusUpdated(event: StreamPlayerEvent) {
        if (event.playerStatus == Status.STOPPED) {
            minecraftClient.inGameHud.setOverlayMessage(
                Text.literal("《${currentSongDetail?.name}》 - ${currentSongDetail?.artists} 已播放完毕"),
                true
            )
            Renderer.loadCover = false
            Renderer.loadSongDetail = false
            Renderer.loadLyric = false
            Renderer.loadCurrentLyric = ""
            Renderer.songInfo = null
            Renderer.registerLoadingCover()
        }
    }

    fun playMusic(lyric: Map<Int, String>, songDetail: SongDetail) {
        this.currentSongDetail = songDetail
        this.lyric = lyric
        val cachedSongFile = File(cacheDir, songDetail.id.toString())
        if (cachedSongFile.exists()) {
            this.open(cachedSongFile)
            this.play()
        } else {
            val songUrl = NCMusic.getSongUrl(songDetail.id)
            URI(songUrl).toURL().openStream().use { inputStream ->
                FileOutputStream(cachedSongFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            this.open(cachedSongFile)
            this.play()
        }
    }
}