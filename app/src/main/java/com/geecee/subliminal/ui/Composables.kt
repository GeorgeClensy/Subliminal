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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geecee.subliminal.R
import com.geecee.subliminal.ui.theme.SubliminalTheme
import com.geecee.subliminal.ui.views.refreshSets
import com.geecee.subliminal.utils.Set
import com.geecee.subliminal.utils.deleteSet
import com.geecee.subliminal.utils.duplicateSet
import com.geecee.subliminal.utils.renameSet

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val onClick: () -> Unit
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
fun SetPreview(set: Set,sets: MutableState<List<Set>>) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current


    // Rename the set
    val showRenameDialog = remember { mutableStateOf(false) }
    if (showRenameDialog.value) {
        TextboxDialog(
            showRenameDialog,
            stringResource(R.string.rename_Set),
            stringResource(R.string.type_here___),
            stringResource(R.string.rename),
            stringResource(R.string.cancel)
        ) { newName ->
            renameSet(context, set.title, newName)
            refreshSets(context,sets)
        }
    }

    Column(
        Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(20.dp, 20.dp, 20.dp, 10.dp)
        ) {


            // Left-aligned text
            Text(
                trimTo25Characters(set.title),
                Modifier.align(Alignment.CenterStart),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Right-aligned icon
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                IconButton(
                    { menuExpanded = true },
                    modifier = Modifier
                        .size(24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        showRenameDialog.value = true
                        menuExpanded = false
                    }, text = {
                        Text(stringResource(R.string.rename))
                    })
                    DropdownMenuItem(onClick = {
                        deleteSet(context, set.title)
                        refreshSets(context,sets)
                        menuExpanded = false
                    }, text = {
                        Text(stringResource(R.string.delete))
                    })
                    DropdownMenuItem(onClick = {
                        duplicateSet(context, set.title, context.resources.getString(R.string.copy_of) + set.title, sets)
                        menuExpanded = false
                    }, text = {
                        Text(stringResource(R.string.duplicate))
                    })
                }
            }
        }

        var mainText = ""
        set.cards.forEach { card ->
            mainText += card.content + ", "
        }

        Text(
            mainText,
            Modifier.padding(20.dp, 0.dp, 20.dp, 20.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }
}

@Composable
fun TextboxDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    placeholder: String,
    submit: String,
    cancel: String,
    onSubmit: (output: String) -> Unit
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = {
            Text(
                title,
                Modifier,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    placeholder = { Text(placeholder) }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                showDialog.value = false
                onSubmit(textState.text)
            }) {
                Text(submit)
            }
        },
        dismissButton = {
            Button(onClick = {
                showDialog.value = false
            }) {
                Text(cancel)
            }
        }
    )
}

@Preview
@Composable
fun PrevTextboxDialog() {
    SubliminalTheme {
        val showDialog = remember { mutableStateOf(false) }

        TextboxDialog(showDialog, "Enter Set Title", "Type here..", "Done", "Cancel") {}
    }
}

fun trimTo25Characters(text: String): String {
    return if (text.length > 25) {
        text.take(25) + "..."
    } else {
        text
    }
}