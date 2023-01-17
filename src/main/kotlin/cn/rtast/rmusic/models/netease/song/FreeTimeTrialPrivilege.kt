/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.song

data class FreeTimeTrialPrivilege(
    val remainTime: Long,
    val resConsumable: Boolean,
    val type: Long,
    val userConsumable: Boolean
)