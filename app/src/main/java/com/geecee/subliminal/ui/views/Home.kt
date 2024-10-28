package com.geecee.subliminal.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geecee.subliminal.R
import com.geecee.subliminal.ui.Carousel
import com.geecee.subliminal.ui.CarouselItem
import com.geecee.subliminal.ui.HomeScreenMessage
import com.geecee.subliminal.ui.theme.SubliminalTheme
import com.geecee.subliminal.utils.getTimeOfDayGreeting

@Composable
fun HomePage() {
    val homePageScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(40, 73, 123, 255),
                        Color(68, 177, 202, 50),
                        MaterialTheme.colorScheme.background
                    ),
                    center = Offset(0f, 0f),
                    radius = 1000f
                )
            )
    ) {
        Column(
            Modifier
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                .verticalScroll(homePageScrollState)
        ) {
            Spacer(Modifier.height(80.dp))

            Text(
                stringResource(R.string.good),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                getTimeOfDayGreeting(stringResource(R.string.morning),stringResource(R.string.afternoon),stringResource(R.string.evening)),
                Modifier.offset(0.dp, (-15).dp),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(25.dp))

            Text(
                stringResource(R.string.recent_sets),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(10.dp))

            val items =
                listOf(
                    CarouselItem(0, R.drawable.placeholdergradient01) {},
                    CarouselItem(1, R.drawable.placeholdergradient02) {},
                    CarouselItem(2, R.drawable.placeholdergradient03) {},
                    CarouselItem(3, R.drawable.placeholdergradient04) {},
                    CarouselItem(4, R.drawable.placeholdergradient05) {},
                )

            Carousel(items)

            Spacer(Modifier.height(25.dp))

            HomeScreenMessage(stringResource(R.string.welcome_to_subliminal), stringResource(R.string.click_the_add_button_to_get_started))

            Spacer(Modifier.height(20.dp))

            Text(
                stringResource(R.string.recommended),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(10.dp))

            val items2 =
                listOf(
                    CarouselItem(0, R.drawable.placeholdergradient05) {},
                    CarouselItem(1, R.drawable.placeholdergradient01) {},
                    CarouselItem(2, R.drawable.placeholdergradient04) {},
                    CarouselItem(3, R.drawable.placeholdergradient03) {},
                    CarouselItem(4, R.drawable.placeholdergradient02) {},
                )

            Carousel(items2)

            Spacer(Modifier.height(25.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePrev() {
    SubliminalTheme {
        Scaffold { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                HomePage()
            }
        }
    }
}