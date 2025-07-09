package com.leoevg.maftimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leoevg.maftimer.navigation.MainNavHost
import com.leoevg.maftimer.screens.timer.TimerScreen
import com.leoevg.maftimer.ui.theme.MafTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MafTimerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainUI(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainUI(modifier: Modifier = Modifier) {
    MainNavHost()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MafTimerTheme {
        TimerScreen({})
    }
}