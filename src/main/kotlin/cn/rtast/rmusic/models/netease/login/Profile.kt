/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.login

data class Profile(
    val accountStatus: Int,
    val artistId: Int,
    val authStatus: Int,
    val authority: Int,
    val avatarDetail: AvatarDetail,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarImgId_str: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val city: Int,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Int,
    val eventCount: Int,
    val expertTags: Any,
    val experts: Experts,
    val followed: Boolean,
    val followeds: Int,
    val follows: Int,
    val gender: Int,
    val mutual: Boolean,
    val nickname: String,
    val playlistBeSubscribedCount: Int,
    val playlistCount: Int,
    val province: Int,
    val remarkName: Any,
    val signature: String,
    val userId: Long,
    val userType: Int,
    val vipType: Int
)