/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/10
 */


package cn.rtast.rmusic.entity.spotify

import com.google.gson.annotations.SerializedName

data class SpotifyLoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
)