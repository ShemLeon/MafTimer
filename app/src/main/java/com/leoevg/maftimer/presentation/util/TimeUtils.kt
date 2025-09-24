package com.leoevg.maftimer.presentation.util

fun Long.formatTime(): String {
    val minutes = (this / 1000) / 60
    val seconds = (this / 1000) % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}