package com.leoevg.maftimer.domain.usecase

import com.leoevg.maftimer.presenter.util.SpotifyAuthManager
import javax.inject.Inject

class CheckAuthorizationUseCase @Inject constructor(
    private val authManager: SpotifyAuthManager
) {
    operator fun invoke(): Boolean {
        return authManager.getStoredToken() != null
    }
}