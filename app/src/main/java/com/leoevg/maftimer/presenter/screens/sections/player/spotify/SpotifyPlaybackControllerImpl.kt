package com.leoevg.maftimer.presenter.screens.sections.player.spotify

import android.content.Context
import android.content.Intent
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.presenter.util.SpotifyAuthManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SpotifyPlaybackControllerImpl  @Inject constructor(
    private val repo: SpotifyRepository,
    private val auth: SpotifyAuthManager,
    @ApplicationContext private val appContext: Context
) : ISpotifyPlaybackController {
    override fun isAuthorized(): Boolean = auth.getStoredToken() != null

    override fun setAccessFromStored() {
        auth.getStoredToken()?.let { repo.setAccessToken(it) }
    }

    override suspend fun play() { repo.play() }
    override suspend fun pause() { repo.pause() }
    override suspend fun next() { repo.next() }
    override suspend fun previous() { repo.previous() }
    override suspend fun seekTo(positionMs: Long) { repo.seekTo(positionMs) }

    override suspend fun getPlayback(): Result<RemotePlayback?> = runCatching {
        val r = repo.getCurrentPlayback().getOrNull() ?: return@runCatching null
        RemotePlayback(
            isPlaying = r.isPlaying ?: false,
            progressMs = r.progressMs ?: 0,
            durationMs = r.item?.durationMs ?: 0,
            title = r.item?.name ?: "",
            artist = r.item?.artists?.getOrNull(0)?.name ?: "",
            albumCoverUrl = r.item?.album?.images?.getOrNull(0)?.url ?: ""
        )
    }

    override fun clearAuthOn401() { auth.clearToken() }

    override fun openSpotifyApp() {
        val intent = appContext.packageManager.getLaunchIntentForPackage("com.spotify.music")
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent != null) appContext.startActivity(intent)
    }

}