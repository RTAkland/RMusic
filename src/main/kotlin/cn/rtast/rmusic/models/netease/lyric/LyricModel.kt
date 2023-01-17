/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/17 14:06
 */

package cn.rtast.rmusic.models.netease.lyric

data class LyricModel(
    val code: Int,
    val klyric: Klyric,
    val lrc: Lrc,
    val qfy: Boolean,
    val romalrc: Romalrc,
    val sfy: Boolean,
    val sgc: Boolean,
    val tlyric: Tlyric
)