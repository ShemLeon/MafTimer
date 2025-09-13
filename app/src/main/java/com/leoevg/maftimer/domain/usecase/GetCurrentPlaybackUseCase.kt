package com.leoevg.maftimer.domain.usecase

import com.leoevg.maftimer.data.api.SpotifyPlaybackState
import com.leoevg.maftimer.domain.repository.ISpotifyRepository
import javax.inject.Inject

class GetCurrentPlaybackUseCase @Inject constructor(
    private val spotifyRepository: ISpotifyRepository
) {
    suspend operator fun invoke(): Result<SpotifyPlaybackState?> {
        return spotifyRepository.getCurrentPlayback()
    }
}