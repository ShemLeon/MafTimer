package com.leoevg.maftimer.customUI

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun CustomCircle(color: Color, radiusSize: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = size.width * radiusSize
        drawCircle(
                color = color,
                radius = radius,
                center = center
        )
    }
}

