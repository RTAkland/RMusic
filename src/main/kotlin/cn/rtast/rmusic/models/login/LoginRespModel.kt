package cn.rtast.rmusic.models.login

data class LoginRespModel(
    val account: Account,
    val bindings: List<Binding>,
    val code: Int,
    val cookie: String,
    val loginType: Int,
    val profile: Profile,
    val token: String
)