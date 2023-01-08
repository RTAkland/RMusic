package cn.rtast.rmusic.data.netease.login

data class LoginResponseModel(
    val account: Account,
    val bindings: List<Binding>,
    val code: Int,
    val cookie: String,
    val loginType: Int,
    val profile: Profile,
    val token: String
)