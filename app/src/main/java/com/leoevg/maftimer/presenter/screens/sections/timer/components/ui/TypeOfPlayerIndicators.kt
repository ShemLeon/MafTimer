package com.leoevg.maftimer.presenter.screens.sections.timer.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TypeOfPlayerIndicators(){
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF121212)
fun IndicatorsPreview() {
    TypeOfPlayerIndicators()
}