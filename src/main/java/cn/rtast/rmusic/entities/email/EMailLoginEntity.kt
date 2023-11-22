package cn.rtast.rmusic.entities.email

data class EMailLoginEntity(
    val profile: Profile,
    val code: Int,
    val cookie: String,
)