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
import cn.rtast.rmusic.entity.PlaylistItem
import cn.rtast.rmusic.enums.MusicPlatform
import cn.rtast.rmusic.scope
import cn.rtast.rmusic.util.mc.Renderer
import cn.rtast.rmusic.util.mc.prefixText
import cn.rtast.rmusic.util.music.KuGouMusic
import cn.rtast.rmusic.util.music.NCMusic
import cn.rtast.rmusic.util.music.QQMusic
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import kotlinx.coroutines.launch
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

    companion object {
        val playlist = mutableListOf<PlaylistItem>()
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
            if (playlist.isNotEmpty()) {
                val latestMusic = playlist[0]
                playlist.removeFirst()
                val songId = latestMusic.id
                scope.launch {
                    val (songUrl, songDetail, lyric) = when (latestMusic.platform) {
                        MusicPlatform.Netease -> {
                            val songUrl = NCMusic.getSongUrl(songId.toLong())
                            val songDetail = NCMusic.getSongDetail(songId.toLong())
                            val lyric = NCMusic.getLyric(songId.toLong())
                            Triple(songUrl, songDetail, lyric)
                        }

                        MusicPlatform.QQ -> {
                            val songUrl = QQMusic.getSongUrl(songId)
                            val songDetail = QQMusic.getSongInfo(songId)
                            val lyric = QQMusic.getLyric(songId)
                            Triple(songUrl, songDetail, lyric)
                        }

                        MusicPlatform.KuGou -> {
                            val songUrl = KuGouMusic.getSongUrl(songId)
                            val songDetail = KuGouMusic.getSongInfo(songId)
                            val lyric = mapOf<Int, String>()
                            Triple(songUrl, songDetail, lyric)
                        }
                    }
                    this@MusicPlayer.playMusic(lyric, songDetail.name, songDetail.artists, songId, songUrl)
                }
            }
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
            minecraftClient.player?.sendMessage(prefixText.append("本地已有缓存, 使用本地缓存进行播放"), false)
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