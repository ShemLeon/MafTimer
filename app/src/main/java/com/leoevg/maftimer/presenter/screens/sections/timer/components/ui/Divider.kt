package com.leoevg.maftimer.presenter.screens.sections.timer.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.leoevg.maftimer.R

@Composable
fun DialDivider(
    angleDegrees: Int,
    color: Color
){
    // Константы габаритов и вдавливания внутрь циферблата
    val inset = 10.dp
    val width = 4.dp
    val height = 12.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                rotationZ = angleDegrees.toFloat(),
                transformOrigin = TransformOrigin.Center
            )
    ){
        Image(
            painter = painterResource(id = R.drawable.rectangle),
            contentDescription = "divider",
            colorFilter = ColorFilter.tint(color),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .align(Alignment.TopCenter)     // радиальный маркер
            //  .offset(y = (0).dp)             // регулировка внутрь/наружу;
                .size(width = 4.dp, height = 12.dp)
        )
    }
}