package cn.rtast.rmusic.models.song

data class FreeTrialPrivilege(
    val cannotListenReason: Any,
    val listenType: Any,
    val resConsumable: Boolean,
    val userConsumable: Boolean
)