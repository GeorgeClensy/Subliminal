@file:OptIn(ExperimentalMaterial3Api::class)

package com.geecee.subliminal.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geecee.subliminal.R
import com.geecee.subliminal.ui.theme.SubliminalTheme

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val onClick: () -> Unit
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(items: List<CarouselItem>) {
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { items.count() },
        modifier = Modifier.fillMaxWidth(),
        preferredItemWidth = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) { i ->
        val item = items[i]

        Image(
            modifier = Modifier
                .height(200.dp)
                .maskClip(MaterialTheme.shapes.extraLarge)
                .combinedClickable(
                    onClick = { item.onClick }, onLongClickLabel = {}.toString(), onLongClick = {}
                ),
            painter = painterResource(id = item.imageResId),
            contentDescription = "Carousel Item",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun PrevCarousel() {
    val items =
        listOf(
            CarouselItem(0, R.drawable.placeholdergradient01) {},
            CarouselItem(1, R.drawable.placeholdergradient02) {},
            CarouselItem(2, R.drawable.placeholdergradient03) {},
            CarouselItem(3, R.drawable.placeholdergradient04) {},
            CarouselItem(4, R.drawable.placeholdergradient05) {},
        )

    Carousel(items)
}

@Composable
fun HomeScreenMessage(title: String, message: String) {
    Row(
        Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Icon(Icons.Rounded.Info, "", Modifier.padding(20.dp, 22.dp), tint = Color.White)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 20.dp, 20.dp)
        ) {
            Text(
                title,
                Modifier,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                message,
                Modifier,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PrevHomeScreenMessage() {
    SubliminalTheme {
        HomeScreenMessage(
            "Test Message",
            "The quick brown fox jumps over the lazy dog as it watches clouds drift by slowly. Stars begin to light up the darkening sky, reflecting off the calm water below. Birds chirp softly in the distance, while leaves rustle as a breeze whispers through. A soft glow appears on the horizon, hinting at the first signs of dawn. The fox pauses, taking in the cool air and peaceful scenery before moving quietly through the trees once again."
        )
    }
}


@Composable
fun SetPreview(set: com.geecee.subliminal.utils.Set) {
    Column(
        Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            Modifier.fillMaxWidth().padding(20.dp,20.dp,20.dp,10.dp)
        ) {
            // Left-aligned text
            Text(
                set.title,
                Modifier.align(Alignment.CenterStart),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Right-aligned icon
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp),
                tint = Color.White
            )
        }

        var mainText = ""
        set.cards.forEach{card->
            mainText += card.content + ", "
        }

        Text(
            mainText,
            Modifier.padding(20.dp,0.dp,20.dp,20.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }

}

@Preview
@Composable
fun PrevSetPreview() {
    SubliminalTheme {
        SetPreview(
            com.geecee.subliminal.utils.Set(
                "Test Set",
                listOf(
                    com.geecee.subliminal.utils.Card("Some text"),
                    com.geecee.subliminal.utils.Card("Some more text"),
                    com.geecee.subliminal.utils.Card("Some even more text"),
                    com.geecee.subliminal.utils.Card("Some final text", "An answer")
                )
            )
        )
    }
}