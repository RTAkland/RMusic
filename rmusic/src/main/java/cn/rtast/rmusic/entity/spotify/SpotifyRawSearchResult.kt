/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/10
 */


package cn.rtast.rmusic.entity.spotify

import com.google.gson.annotations.SerializedName

data class SpotifyRawSearchResult(
    val tracks: Tracks
) {
    data class Tracks(
        val items: List<Item>
    )

    data class Item(
        val name: String,
        val artists: List<Artist>,
        @SerializedName("duration_ms")
        val durationMs: Long,
        val id: String,
    )

    data class Artist(
        val name: String,
    )
}