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

object LyricParser {

    private fun toTimeInSeconds(input: String): Int {
        val timeParts = input.split(":")
        val minutes = timeParts[0].toInt()
        val seconds = timeParts[1].substringBefore(".").toInt()
        return minutes * 60 + seconds
    }

    fun parse(lyric: String): MutableMap<Int, String> {
        val lyricsMap = mutableMapOf<Int, String>()
        var currentLyric = ""
        for (line in lyric.trimIndent().lines()) {
            val parts = line.split("]")
            val times = parts.dropLast(1).map { toTimeInSeconds(it.substring(1).replace(".000", "").trim()) }
            val content = parts.last().trim()
            if (times.isNotEmpty()) {
                for (time in times) {
                    lyricsMap[time] = currentLyric.trim()
                }
                currentLyric = content
            } else {
                currentLyric += " $content"
            }
        }
        val keys = lyricsMap.keys.toList()
        val values = lyricsMap.values.toList()
        keys.dropLast(1)
        val shiftedValues = values.drop(1)
        val shiftedLyricsMap = mutableMapOf<Int, String>()
        for (i in keys.indices) {
            shiftedLyricsMap[keys[i]] = shiftedValues.getOrElse(i) { "" }
        }
        return shiftedLyricsMap
    }
}
