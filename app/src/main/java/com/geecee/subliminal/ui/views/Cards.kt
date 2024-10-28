package com.geecee.subliminal.ui.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.geecee.subliminal.R
import com.geecee.subliminal.ui.CardEditor
import com.geecee.subliminal.ui.TextboxDialog
import com.geecee.subliminal.utils.Card
import com.geecee.subliminal.utils.Set
import com.geecee.subliminal.utils.addCardToSet
import com.geecee.subliminal.utils.deleteSet
import com.geecee.subliminal.utils.duplicateSet
import com.geecee.subliminal.utils.getCardsInSet
import com.geecee.subliminal.utils.renameSet

fun refreshCards(context: Context, cardsState: MutableState<List<Card>>, setTitle: String) {
    cardsState.value = getCardsInSet(context, setTitle)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsPage(
    set: MutableState<Set>,
    navController: NavController,
    setsState: MutableState<List<Set>>,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val showEditor = remember {
        mutableStateOf(false)
    }
    val cards = remember {
        mutableStateOf(set.value.cards)
    }
    var menuExpanded by remember { mutableStateOf(false) }
    val setTitle = remember { mutableStateOf(set.value.title) }
    val showRenameDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        setTitle.value,
                        Modifier,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                        refreshSets(context, setsState)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showEditor.value = true
                        refreshSets(context, setsState)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                    IconButton(onClick = {
                        menuExpanded = true
                        refreshSets(context, setsState)
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                            refreshSets(context, setsState)}
                    ) {
                        DropdownMenuItem(onClick = {
                            showRenameDialog.value = true
                            menuExpanded = false
                            refreshSets(context, setsState)
                        }, text = {
                            Text(stringResource(R.string.rename))
                        })
                        DropdownMenuItem(onClick = {
                            deleteSet(context, set.value.title)
                            navController.popBackStack()
                            refreshSets(context, setsState)
                            menuExpanded = false
                        }, text = {
                            Text(stringResource(R.string.delete))
                        })
                        DropdownMenuItem(onClick = {
                            duplicateSet(
                                context,
                                set.value.title,
                                context.resources.getString(R.string.copy_of) + set.value.title,
                                setsState
                            )
                            menuExpanded = false
                            refreshSets(context, setsState)
                        }, text = {
                            Text(stringResource(R.string.duplicate))
                        })
                    }
                }
            )
        },
        modifier = Modifier.padding(20.dp, 65.dp, 20.dp, 0.dp),
        content = { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
            ) {
                items(cards.value) { card ->
                    com.geecee.subliminal.ui.Card(
                        card,
                        context.resources.getString(R.string.card),
                        set.value,
                        cards,
                        snackbarHostState
                    )

                    Spacer(Modifier.height(15.dp))
                }

                item {
                    Spacer(Modifier.height(300.dp))
                }
            }

            var newCard = Card("")
            if (showEditor.value) {
                CardEditor(
                    showEditor,
                    stringResource(R.string.new_card),
                    stringResource(R.string.card_content),
                    stringResource(R.string.card_answer),
                    stringResource(R.string.done),
                    stringResource(R.string.cancel),
                    newCard,
                    snackbarHostState
                ) { card ->
                    newCard = card
                    addCardToSet(
                        context,
                        set.value.title,
                        newCard.content,
                        newCard.contentAnswer
                    )
                    refreshCards(context, cards, set.value.title)
                    refreshSets(context, setsState)
                }
            }

            if (showRenameDialog.value) {
                TextboxDialog(
                    showRenameDialog,
                    stringResource(R.string.rename_Set),
                    stringResource(R.string.type_here___),
                    stringResource(R.string.rename),
                    stringResource(R.string.cancel)
                ) { newName ->
                    renameSet(context, set.value.title, newName)
                    setTitle.value = newName
                    refreshSets(context, setsState)
                }
            }


        }
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background
                    ),
                    startY = 1200F
                )
            )
    )
}