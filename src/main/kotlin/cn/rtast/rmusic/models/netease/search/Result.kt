/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.search

data class Result(
    val hasMore: Boolean,
    val songCount: Long,
    val songs: List<Song>
)