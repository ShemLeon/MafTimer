package com.leoevg.maftimer.data.api

import com.google.gson.annotations.SerializedName

data class SpotifyPlaybackState(
    @SerializedName("is_playing")
    val isPlaying: Boolean,

    @SerializedName("progress_ms")
    val progressMs: Long?,

    val item: SpotifyTrack?
)