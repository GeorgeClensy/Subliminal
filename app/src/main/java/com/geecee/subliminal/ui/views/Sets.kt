package com.geecee.subliminal.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geecee.subliminal.ui.theme.SubliminalTheme

@Composable
fun SetsPage() {
    val setsPageScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                .verticalScroll(setsPageScrollState)
        ) {
            Spacer(Modifier.height(80.dp))

            Text(
                "Manage Sets",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(25.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevSetsPage() {
    SubliminalTheme {
        Scaffold { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                SetsPage()
            }
        }
    }
}