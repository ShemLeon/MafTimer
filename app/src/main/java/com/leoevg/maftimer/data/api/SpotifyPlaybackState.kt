package com.leoevg.maftimer.data.api

import com.google.gson.annotations.SerializedName

data class SpotifyPlaybackState(
    @SerializedName("is_playing")
    val isPlaying: Boolean,

    @SerializedName("progress_ms")
    val progressMs: Long?,

    val item: SpotifyTrack?
)

data class SpotifyTrack(
    val name: String,
    val artists: List<SpotifyArtist>,
    val album: SpotifyAlbum,
    @SerializedName("duration_ms")
    val durationMs: Long
)

data class SpotifyArtist(
    val name: String
)

data class SpotifyAlbum(
    val name: String,
    val images: List<SpotifyImage>
)

data class SpotifyImage(
    val url: String,
    val height: Int,
    val width: Int
)