/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.entity.ncm

data class GetUserAccount(
    val account: Account
) {
    data class Account(
        val profile: Profile
    )

    data class Profile(
        val nickname: String,
    )
}