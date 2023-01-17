/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.search

data class Song(
    val album: Album,
    val alias: List<Any>,
    val artists: List<ArtistX>,
    val copyrightId: Long,
    val duration: Long,
    val fee: Long,
    val ftype: Long,
    val id: Long,
    val mark: Long,
    val mvid: Long,
    val name: String,
    val rUrl: Any,
    val rtype: Long,
    val status: Long
)