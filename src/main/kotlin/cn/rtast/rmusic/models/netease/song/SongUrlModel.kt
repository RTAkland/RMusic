/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.song

data class SongUrlModel(
    val code: Int,
    val `data`: List<Data>
)