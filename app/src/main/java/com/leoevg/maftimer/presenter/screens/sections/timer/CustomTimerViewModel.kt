package com.leoevg.maftimer.presenter.screens.sections.timer

import androidx.lifecycle.ViewModel
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.util.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CustomTimerViewModel: ViewModel() {
    private val _state = MutableStateFlow(CustomTimerState())
    val state: StateFlow<CustomTimerState> = _state.asStateFlow()


}


