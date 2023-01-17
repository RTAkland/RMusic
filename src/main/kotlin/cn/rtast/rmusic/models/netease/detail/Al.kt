/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.detail

data class Al(
    val id: Long,
    val name: String,
    val pic: Long,
    val picUrl: String,
    val tns: List<Any>
)