package com.leoevg.maftimer.presenter.screens.sections.player.spotify

import android.content.Context
import android.content.Intent
import com.leoevg.maftimer.domain.repository.ISpotifyRepository
import com.leoevg.maftimer.domain.usecase.PlayMusicUseCase
import com.leoevg.maftimer.domain.usecase.PauseMusicUseCase
import com.leoevg.maftimer.domain.usecase.NextSongUseCase
import com.leoevg.maftimer.domain.usecase.PreviousSongUseCase
import com.leoevg.maftimer.domain.usecase.SeekToPositionUseCase
import com.leoevg.maftimer.domain.usecase.GetCurrentPlaybackUseCase
import com.leoevg.maftimer.presenter.util.SpotifyAuthManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SpotifyPlaybackControllerImpl @Inject constructor(
    private val repo: ISpotifyRepository,
    private val auth: SpotifyAuthManager,
    private val playMusic: PlayMusicUseCase,
    private val pauseMusic: PauseMusicUseCase,
    private val nextSong: NextSongUseCase,
    private val previousSong: PreviousSongUseCase,
    private val seekToPosition: SeekToPositionUseCase,
    private val getCurrentPlayback: GetCurrentPlaybackUseCase,
    @ApplicationContext private val appContext: Context
) : ISpotifyPlaybackController {
    override fun isAuthorized(): Boolean = auth.getStoredToken() != null
    override suspend fun play() { playMusic() }
    override suspend fun pause() { pauseMusic() }
    override suspend fun next() { nextSong() }
    override suspend fun previous() { previousSong() }
    override suspend fun seekTo(positionMs: Long) { seekToPosition(positionMs) }
    override fun setAccessFromStored() {auth.getStoredToken()?.let { repo.setAccessToken(it) }}

    override suspend fun getPlayback(): Result<RemotePlayback?> = runCatching {
        val r = getCurrentPlayback().getOrNull() ?: return@runCatching null
        RemotePlayback(
            isPlaying = r.isPlaying,
            progressMs = r.progressMs ?: 0,
            durationMs = r.item?.durationMs ?: 0,
            title = r.item?.name ?: "",
            artist = r.item?.artists?.getOrNull(0)?.name ?: "",
            albumCoverUrl = r.item?.album?.images?.getOrNull(0)?.url ?: ""
        )
    }

    override fun clearAuthOn401() {
        auth.clearToken()
    }

    override fun openSpotifyApp() {
        val intent = appContext.packageManager.getLaunchIntentForPackage("com.spotify.music")
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent != null) appContext.startActivity(intent)
    }

}