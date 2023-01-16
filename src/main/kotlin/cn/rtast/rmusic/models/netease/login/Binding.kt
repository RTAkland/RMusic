/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.login

data class Binding(
    val bindingTime: Long,
    val expired: Boolean,
    val expiresIn: Long,
    val id: Long,
    val refreshTime: Long,
    val tokenJsonStr: String,
    val type: Long,
    val url: String,
    val userId: Long
)