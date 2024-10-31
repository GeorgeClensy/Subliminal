package com.geecee.subliminal.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.geecee.subliminal.R
import com.geecee.subliminal.ui.AllSets
import com.geecee.subliminal.ui.SetScrollListItemInfo
import com.geecee.subliminal.ui.SetSelector
import com.geecee.subliminal.ui.SetsScrollListItem
import com.geecee.subliminal.utils.Set

@Composable
fun StartPage(
    navController: NavController,
    sets: MutableState<List<Set>>,
    selectedSets: MutableState<List<Set>>
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
        ) {
            item {
                Spacer(
                    Modifier
                        .height(80.dp)
                        .padding(20.dp, 0.dp, 20.dp, 0.dp)
                )
            }

            item {
                Text(
                    stringResource(R.string.Start),
                    Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Spacer(
                    Modifier
                        .height(25.dp)
                        .padding(20.dp, 0.dp, 20.dp, 0.dp)
                )
            }

            item {
                Text(
                    stringResource(R.string.choose_sets),
                    Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                val items = sets.value
                val itemsAsSetScrollListItem = mutableListOf<SetScrollListItemInfo>()
                var i = 1
                items.forEach{item ->
                    itemsAsSetScrollListItem.add(SetScrollListItemInfo(item,selectedSets.value.contains(item),i))
                    i++
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp)
                ) {
                    items(itemsAsSetScrollListItem.take(5)) { set ->
                        SetsScrollListItem(set) { checked ->
                            val updatedSelections = selectedSets.value.toMutableList()
                            if (checked) {
                                updatedSelections.add(set.set)
                            } else {
                                updatedSelections.remove(set.set)
                            }
                            selectedSets.value = updatedSelections
                        }
                    }
                }
            }

            if (sets.value.size > 5) {
                item {
                    Box(
                        Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)
                    ) {
                        AllSets {
                            navController.navigate("SelectSets")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllSetsSelect(
    navController: NavController,
    setsState: MutableState<List<Set>>,
    selectedSets: MutableState<List<Set>>
) {
    val context = LocalContext.current
    Surface(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.choose_sets),
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
                    }
                )
            },
            modifier = Modifier.padding(20.dp, 65.dp, 20.dp, 0.dp),
            content = { paddingValues ->
                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
                ) {
                    items(setsState.value) { set ->
                        SetSelector(set, selectedSets.value.contains(set)) { checked ->
                            val updatedSelections = selectedSets.value.toMutableList()
                            if (checked) {
                                updatedSelections.add(set)
                            } else {
                                updatedSelections.remove(set)
                            }
                            selectedSets.value = updatedSelections
                        }
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
}