/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.search

data class Album(
    val artist: ArtistX,
    val copyrightId: Long,
    val id: Long,
    val mark: Long,
    val name: String,
    val picId: Long,
    val publishTime: Long,
    val size: Long,
    val status: Long
)