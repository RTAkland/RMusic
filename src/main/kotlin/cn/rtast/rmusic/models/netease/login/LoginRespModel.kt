package cn.rtast.rmusic.models.netease.login

data class LoginRespModel(
    val account: cn.rtast.rmusic.models.netease.login.Account,
    val bindings: List<cn.rtast.rmusic.models.netease.login.Binding>,
    val code: Int,
    val cookie: String,
    val loginType: Int,
    val profile: cn.rtast.rmusic.models.netease.login.Profile,
    val token: String
)