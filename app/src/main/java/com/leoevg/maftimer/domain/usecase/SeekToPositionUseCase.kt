package com.leoevg.maftimer.domain.usecase

import com.leoevg.maftimer.domain.repository.ISpotifyRepository
import javax.inject.Inject

class SeekToPositionUseCase @Inject constructor(
    private val spotifyRepository: ISpotifyRepository
) {
    suspend operator fun invoke(positionMs: Long): Result<Unit> {
        return spotifyRepository.seekTo(positionMs)
    }
}