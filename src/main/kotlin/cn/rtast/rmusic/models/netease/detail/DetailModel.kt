/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.detail

data class DetailModel(
    val code: Int,
    val privileges: List<Privilege>,
    val songs: List<Song>
)