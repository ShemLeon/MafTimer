package com.leoevg.maftimer.presenter.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainScreenViewModel: ViewModel() {
    var time by mutableStateOf(0L)

    fun onEvent(event: MainScreenEvent){

    }
    fun pushBtn(){

    }
}