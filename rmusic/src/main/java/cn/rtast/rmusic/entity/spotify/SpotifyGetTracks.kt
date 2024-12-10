/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/10
 */


package cn.rtast.rmusic.entity.spotify

data class SpotifyGetTracks(
    val album: Album
) {
    data class Album(
        val artists: List<SpotifyRawSearchResult.Artist>,
        val images: List<Image>,
        val name: String,
    )

    data class Image(
        val url: String,
    )
}