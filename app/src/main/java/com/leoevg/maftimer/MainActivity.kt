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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
    }

    private fun connected() {
        // Then we will write some more code here.
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }

}


