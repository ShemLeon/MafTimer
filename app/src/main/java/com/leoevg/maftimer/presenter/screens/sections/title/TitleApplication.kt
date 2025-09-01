package com.leoevg.maftimer.presenter.screens.sections.title

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleApplication(){
    Row(
    modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = "Timer Screen",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF121212)
fun TitleApplicationPreview() {
    TitleApplication()
}