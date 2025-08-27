package com.leoevg.maftimer.presenter.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun CustomCircle(
    color: Color,
    diameterFraction: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = size.width * diameterFraction / 2f
        drawCircle(
                color = color,
                radius = radius,
                center = center
        )
    }
}

