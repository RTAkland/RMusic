/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.login

data class Account(
    val anonimousUser: Boolean,
    val ban: Long,
    val baoyueVersion: Long,
    val createTime: Long,
    val donateVersion: Long,
    val id: Long,
    val salt: String,
    val status: Long,
    val tokenVersion: Long,
    val type: Long,
    val uninitialized: Boolean,
    val userName: String,
    val vipType: Long,
    val viptypeVersion: Long,
    val whitelistAuthority: Long
)