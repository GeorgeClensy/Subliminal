@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("unused")

package com.geecee.subliminal.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.geecee.subliminal.ui.views.refreshCards
import com.geecee.subliminal.ui.views.refreshSets
import com.geecee.subliminal.utils.Card
import com.geecee.subliminal.utils.Set
import com.geecee.subliminal.utils.deleteCardFromSet
import com.geecee.subliminal.utils.deleteSet
import com.geecee.subliminal.utils.duplicateSet
import com.geecee.subliminal.utils.editCardInSet
import com.geecee.subliminal.utils.getSetByTitle
import com.geecee.subliminal.utils.renameSet
import kotlinx.coroutines.launch

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val onClick: () -> Unit
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Carousel(items: List<CarouselItem>) {
    if (items.isNotEmpty()) {
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
                        onClick = { item.onClick },
                        onLongClickLabel = {}.toString(),
                        onLongClick = {}
                    ),
                painter = painterResource(id = item.imageResId),
                contentDescription = "Carousel Item",
                contentScale = ContentScale.Crop
            )
        }
    }
}

//@Preview
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

//@Preview
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
fun SetPreview(set: Set, sets: MutableState<List<Set>>, modifier: Modifier) {
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
            refreshSets(context, sets)
        }
    }

    Column(
        modifier.then(
            Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(20.dp, 20.dp, 20.dp, 10.dp)
        ) {


            // Left-aligned text
            Text(
                trimString(set.title, 25),
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
                        refreshSets(context, sets)
                        menuExpanded = false
                    }, text = {
                        Text(stringResource(R.string.delete))
                    })
                    DropdownMenuItem(onClick = {
                        duplicateSet(
                            context,
                            set.title,
                            context.resources.getString(R.string.copy_of) + set.title,
                            sets
                        )
                        menuExpanded = false
                    }, text = {
                        Text(stringResource(R.string.duplicate))
                    })
                }
            }
        }

        var mainText = ""
        var i = 1
        set.cards.forEach { card ->
            mainText += if (i == set.cards.size) {
                card.content
            } else {
                card.content + ", "
            }
            i++
        }

        Text(
            trimString(mainText, 100),
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

//@Preview
@Composable
fun PrevTextboxDialog() {
    SubliminalTheme {
        val showDialog = remember { mutableStateOf(false) }

        TextboxDialog(showDialog, "Enter Set Title", "Type here..", "Done", "Cancel") {}
    }
}

fun trimString(text: String, number: Int): String {
    return if (text.length > number) {
        text.take(number) + "..."
    } else {
        text
    }
}

@Composable
fun Card(
    card: Card, title: String, set: MutableState<Set>, cards: MutableState<List<Card>>,
    snackbarHostState: SnackbarHostState
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showEditDialog = remember {
        mutableStateOf(false)
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
                title,
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
                        menuExpanded = false
                        showEditDialog.value = true
                        refreshCards(context, cards, set.value.title)
                    }, text = {
                        Text(stringResource(R.string.edit))
                    })
                    DropdownMenuItem(onClick = {
                        menuExpanded = false
                        deleteCardFromSet(context, set.value.title, card.content)
                        refreshCards(context, cards, set.value.title)
                    }, text = {
                        Text(stringResource(R.string.delete))
                    })
                }
            }
        }

        Column {
            Text(
                card.content,
                Modifier.padding(20.dp, 0.dp, 20.dp, 20.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )

            if (card.contentAnswer != "") {
                Text(
                    card.contentAnswer!!,
                    Modifier.padding(20.dp, 0.dp, 20.dp, 20.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        }
    }

    if (showEditDialog.value) {
        card.contentAnswer?.let {
            CardEditor(
                showEditDialog,
                stringResource(R.string.edit_card),
                card.content,
                it,
                stringResource(R.string.edit),
                stringResource(R.string.cancel),
                card,
                snackbarHostState,
                set
            ) { newContent ->
                editCardInSet(
                    context,
                    set.value.title,
                    card.content,
                    newContent.content,
                    newContent.contentAnswer
                )

                refreshCards(context, cards, set.value.title)
            }
        }
    }
}

@Composable
fun CardEditor(
    showDialog: MutableState<Boolean>,
    title: String,
    placeholder: String,
    answerPlaceholder: String,
    submit: String,
    cancel: String,
    inputCard: Card,
    snackbarHostState: SnackbarHostState,
    set: MutableState<Set>,
    onSubmit: (card: Card) -> Unit
) {
    val context = LocalContext.current
    var textState by remember { mutableStateOf(TextFieldValue(inputCard.content)) }
    var ansTextState by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    if (inputCard.contentAnswer != null) {
        ansTextState = TextFieldValue(inputCard.contentAnswer!!)
    }

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
                    placeholder = { Text(placeholder) },
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp)
                )

                TextField(
                    value = ansTextState,
                    onValueChange = { ansTextState = it },
                    placeholder = { Text(answerPlaceholder) }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                set.value = getSetByTitle(context, set.value.title)


                var alreadyExists = false

                set.value.cards.forEach { card ->
                    if (card.content == textState.text) {
                        alreadyExists = true
                    }
                }

                if (textState.text == "") {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Please enter main content",
                            actionLabel = "Ok"
                        )
                    }
                } else if (alreadyExists) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            "Card with this content already exists",
                            "Ok"
                        )
                    }
                } else {
                    showDialog.value = false
                    onSubmit(Card(textState.text, ansTextState.text))
                }
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

@Composable
fun SetSelector(set: Set, checked: Boolean?, checkChanged: (checked: Boolean) -> Unit) {
    val clicked = remember { mutableStateOf(false) }

    if (checked == true) {
        clicked.value = checked
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Checkbox(clicked.value, { checked ->
            clicked.value = checked
            checkChanged(checked)
        })
        Text(
            text = trimString(set.title, 27),
            Modifier.padding(0.dp, 8.dp),
            color = Color.White,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

//@Preview
@Composable
fun PrevSetSelector() {
    SubliminalTheme {
        SetSelector(Set("Title", null, listOf()), false) {}
    }
}

@Composable
fun AllSets(clickable: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                clickable()
            }) {
        Text(
            text = "All sets",
            Modifier.padding(0.dp, 8.dp),
            color = Color.White,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            style = MaterialTheme.typography.titleLarge
        )
        Icon(
            Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            "",
            Modifier.align(Alignment.CenterEnd),
            Color.White
        )
    }
}

//@Preview
@Composable
fun PrevAllSets() {
    SubliminalTheme {
        AllSets {}
    }
}

data class SetScrollListItemInfo(
    val set: Set,
    var checked: Boolean,
    var placeholderImage: Int,
)

@Composable
fun SetsScrollListItem(
    setsScrollListItemInfo: SetScrollListItemInfo,
    onCheckChanged: (checked: Boolean) -> Unit
) {
    var checkedState by remember { mutableStateOf(setsScrollListItemInfo.checked) }
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .fillMaxSize()
            .clip(MaterialTheme.shapes.extraLarge)
            .clickable {
                checkedState = !checkedState
            }
    ) {


        Image(
            painter = painterResource(
                id = when (setsScrollListItemInfo.placeholderImage) {
                    1 -> R.drawable.placeholdergradient01
                    2 -> R.drawable.placeholdergradient02
                    3 -> R.drawable.placeholdergradient03
                    4 -> R.drawable.placeholdergradient04
                    5 -> R.drawable.placeholdergradient05
                    else -> R.drawable.placeholdergradient03
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    checkedState = !checkedState
                }
                .align(Alignment.BottomStart)
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = checkedState, onCheckedChange = {
                checkedState = it
                onCheckChanged(it)
            }
            )

            Text(
                text = setsScrollListItemInfo.set.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


@Preview
@Composable
fun PrevSetsScrollListItem() {
    SubliminalTheme {
        val setInfo = SetScrollListItemInfo(
            Set(
                "A set with a very long name",
                null
            ),
            false,
            1
        )

        SetsScrollListItem(
            setInfo,
        ) {}
    }
}