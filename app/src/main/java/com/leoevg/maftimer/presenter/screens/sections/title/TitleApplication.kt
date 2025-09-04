package com.leoevg.maftimer.presenter.screens.sections.title

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.R

@Composable
fun TitleApplication() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Timer Screen",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center
        )
        Icon(
            painter = painterResource(R.drawable.btn_settings),
            contentDescription = "Settings",
            tint = Color(0xFFFFFFFF),
            modifier = Modifier
                .size(30.dp)
                .padding()
                .clickable {
//                        if (isAuthorized) viewModel.previous()
                },
        )

    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF121212)
fun TitleApplicationPreview() {
    TitleApplication()
}