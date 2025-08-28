package com.leoevg.maftimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.leoevg.maftimer.navigation.MainNavHost
import com.leoevg.maftimer.ui.theme.MafTimerTheme
import com.leoevg.maftimer.presenter.util.HideSystemBars


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MafTimerTheme {
                    MainNavHost(
                        modifier = Modifier.fillMaxSize()
                    )
                    HideSystemBars()
            }
        }
    }
}


