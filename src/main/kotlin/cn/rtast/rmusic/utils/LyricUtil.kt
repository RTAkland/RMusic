/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/17 14:06
 */

package cn.rtast.rmusic.utils

import cn.rtast.rmusic.models.netease.lyric.ext.ExtendedLyricModel
import cn.rtast.rmusic.models.netease.lyric.ext.Lyric
import cn.rtast.rmusic.models.netease.lyric.LyricModel
import com.google.gson.Gson
import java.net.URL

class LyricUtil {

    private val gson = Gson()

    fun parse(originLyric: String): MutableList<Lyric> {
        val lyrics = gson.fromJson(originLyric, LyricModel::class.java).lrc.lyric.replace(".000", "").split("\n")
        val lyricList = ExtendedLyricModel(mutableListOf())
        lyrics.forEach {
            if (it.isNotEmpty()) {
                val lastBracket = it.lastIndexOf("]")
                val tp = it.substring(0..lastBracket)
                val lyric = it.substring(tp.length)
                tp.split("]").forEach { t ->
                    val time = t.replace("[", "").split(":")
                    try {
                        val minute = time.first().toInt()
                        val second = time.last().toInt()
                        val total = minute * 60 + second
                        lyricList.lyrics.add(Lyric(total, lyric))
                    } catch (_: NumberFormatException) {
                    }
                }
            }
        }
        lyricList.lyrics.sortBy { it.tp }
        return lyricList.lyrics
    }
}
