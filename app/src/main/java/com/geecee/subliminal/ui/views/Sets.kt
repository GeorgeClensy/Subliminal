package com.geecee.subliminal.ui.views

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.geecee.subliminal.R
import com.geecee.subliminal.ui.SetPreview
import com.geecee.subliminal.utils.Set
import com.geecee.subliminal.utils.loadSets

fun refreshSets(context: Context, setsState: MutableState<List<Set>>) {
    setsState.value = loadSets(context)
}

@Composable
fun SetsPage(sets: MutableState<List<Set>>, currentSet: MutableState<Set>, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
        ) {
            item {
                Spacer(Modifier.height(80.dp))
            }

            item {
                Text(
                    stringResource(R.string.manage_sets),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Spacer(Modifier.height(25.dp))
            }

            items(sets.value) { set ->
                SetPreview(
                    set,
                    sets,
                    Modifier.clickable {
                        navController.navigate("Cards")
                        currentSet.value = set
                    }
                )

                Spacer(Modifier.height(15.dp))
            }
        }
    }
}