/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.search

data class ArtistX(
    val albumSize: Long,
    val alias: List<Any>,
    val fansGroup: Any,
    val id: Long,
    val img1v1: Long,
    val img1v1Url: String,
    val name: String,
    val picId: Int,
    val picUrl: Any,
    val trans: Any
)