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

import cn.rtast.rmusic.cacheDir
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class MusicPlayer : StreamPlayerListener, StreamPlayer() {

    private var lyric: Map<Int, String>? = null
    private val minecraftClient: MinecraftClient = MinecraftClient.getInstance()
    private var currentSongName: String? = null
    private var currentArtistName: String? = null

    init {
        this.addStreamPlayerListener(this)
    }

    override fun opened(dataSource: Any, properties: MutableMap<String, Any>) {
        minecraftClient.soundManager.stopAll()
        Renderer.renderLyric()
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
            }
        }
    }

    override fun statusUpdated(event: StreamPlayerEvent) {
        if (event.playerStatus == Status.STOPPED) {
            minecraftClient.inGameHud.setOverlayMessage(
                Text.literal("《${this.currentSongName}》 by: ${this.currentArtistName} 已播放完毕"),
                true
            )
            Renderer.loadCover = false
            Renderer.loadSongDetail = false
            Renderer.loadLyric = false
            Renderer.loadCurrentLyric = ""
            Renderer.registerLoadingCover()
        }
    }

    fun playMusic(lyric: Map<Int, String>, songName: String, songArtist: String, songId: String, songUrl: String) {
        this.lyric = lyric
        this.currentSongName = songName
        this.currentArtistName = songArtist
        Renderer.currentSongName = songName
        Renderer.currentArtistName = songArtist
        val cachedSongFile = File(cacheDir, songId)
        if (cachedSongFile.exists()) {
            this.open(cachedSongFile)
            this.play()
        } else {
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