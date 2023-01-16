/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.login

data class LoginRespModel(
    val account: Account,
    val bindings: List<Binding>,
    val code: Int,
    val cookie: String,
    val loginType: Long,
    val profile: Profile,
    val token: String
)