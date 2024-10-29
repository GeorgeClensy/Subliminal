package com.geecee.subliminal.ui.views

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
import com.geecee.subliminal.R
import com.geecee.subliminal.ui.AllSets
import com.geecee.subliminal.ui.SetSelector
import com.geecee.subliminal.utils.Set

@Composable
fun StartPage(sets: MutableState<List<Set>>) {
    val selectedSets: MutableList<String> = mutableListOf()

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
                    stringResource(R.string.Start),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Spacer(Modifier.height(25.dp))
            }

            item {
                Text(
                    stringResource(R.string.choose_sets),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            var i = 1
            items(sets.value) { set ->
                if (i < 5) {
                    SetSelector(set) { checked ->
                        if (checked) {
                            selectedSets.add(set.title)
                        } else {
                            selectedSets.remove(set.title)
                        }
                    }
                }
                i++
            }

            if(sets.value.size > 5){
                item{
                    AllSets {
                        //Navigate
                    }
                }
            }
        }
    }
}