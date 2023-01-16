/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.login

data class Profile(
    val accountStatus: Long,
    val artistId: Long,
    val authStatus: Long,
    val authority: Long,
    val avatarDetail: AvatarDetail,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarImgId_str: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val city: Long,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Long,
    val eventCount: Long,
    val expertTags: Any,
    val experts: Experts,
    val followed: Boolean,
    val followeds: Long,
    val follows: Long,
    val gender: Long,
    val mutual: Boolean,
    val nickname: String,
    val playlistBeSubscribedCount: Long,
    val playlistCount: Long,
    val province: Long,
    val remarkName: Any,
    val signature: String,
    val userId: Long,
    val userType: Long,
    val vipType: Long
)