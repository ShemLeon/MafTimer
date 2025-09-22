package com.leoevg.maftimer.data.api

import retrofit2.Response
import retrofit2.http.*

interface SpotifyApi {

    @GET("me/player")
    suspend fun getCurrentPlayback(@Header("Authorization") token: String): Response<SpotifyPlaybackState>

    @PUT("me/player/play")
    suspend fun resumePlayback(@Header("Authorization") token: String): Response<Unit>

    @PUT("me/player/pause")
    suspend fun pausePlayback(@Header("Authorization") token: String): Response<Unit>

    @POST("me/player/next")
    suspend fun skipToNext(@Header("Authorization") token: String): Response<Unit>

    @POST("me/player/previous")
    suspend fun skipToPrevious(@Header("Authorization") token: String): Response<Unit>

    @PUT("me/player/seek")
    suspend fun seekToPosition(
        @Header("Authorization") token: String,
        @Query("position_ms") positionMs: Long
    ): Response<Unit>
}