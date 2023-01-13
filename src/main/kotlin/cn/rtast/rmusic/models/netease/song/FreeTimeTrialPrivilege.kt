package cn.rtast.rmusic.models.netease.song

data class FreeTimeTrialPrivilege(
    val remainTime: Int,
    val resConsumable: Boolean,
    val type: Int,
    val userConsumable: Boolean
)