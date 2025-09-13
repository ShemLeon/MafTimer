package com.leoevg.maftimer.domain.usecase

import com.leoevg.maftimer.domain.repository.ISpotifyRepository
import javax.inject.Inject

class PlayMusicUseCase @Inject constructor(
    private val spotifyRepository: ISpotifyRepository
){
    suspend operator fun invoke(): Result<Unit> {
        return spotifyRepository.play()
    }
}