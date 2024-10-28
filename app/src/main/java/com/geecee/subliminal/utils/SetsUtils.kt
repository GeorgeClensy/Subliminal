package com.geecee.subliminal.utils

import android.content.Context
import androidx.compose.runtime.MutableState
import com.geecee.subliminal.ui.views.refreshSets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

data class Set(
    var title: String,
    val lastRevised: Date? = null,
    val cards: List<Card> = listOf() // Store cards directly in the set
)

data class Card(
    val content: String,
    val contentAnswer: String? = null
)

fun saveSets(context: Context, sets: List<Set>) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    val json = Gson().toJson(sets)
    editor.putString("sets_key", json)
    editor.apply()
}

fun loadSets(context: Context): List<Set> {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("sets_key", null)

    return if (json != null) {
        val type = object : TypeToken<List<Set>>() {}.type
        Gson().fromJson(json, type)
    } else {
        emptyList()
    }
}

fun createSet(
    context: Context,
    title: String,
    lastRevised: Date? = null,
    cards: List<Card> = emptyList()
) {
    val sets = loadSets(context).toMutableList()

    val newSet = Set(
        title = title,
        lastRevised = lastRevised ?: Date(),
        cards = cards
    )

    sets.add(newSet)
    saveSets(context, sets)
}

fun renameSet(context: Context, currentTitle: String, newTitle: String) {
    val sets = loadSets(context).toMutableList()

    val setToRename = sets.find { it.title == currentTitle }
    setToRename?.let {
        it.title = newTitle
        saveSets(context, sets)
    }
}

fun deleteSet(context: Context, title: String) {
    val sets = loadSets(context).toMutableList()

    val updatedSets = sets.filter { it.title != title } // Filter out the set to delete
    saveSets(context, updatedSets) // Save the updated list back to SharedPreferences
}

//TODO: look into the title stuff here
fun duplicateSet(context: Context, title: String, newTitle: String? = null, pageSets: MutableState<List<Set>>) {
    val sets = loadSets(context).toMutableList()

    val setToDuplicate = sets.find { it.title == title }
    setToDuplicate?.let { originalSet ->
        // Create a duplicate with a new ID and optional new title
        val duplicatedSet = originalSet.copy(
            title = newTitle ?: "${originalSet.title} (Copy)"
        )

        sets.add(duplicatedSet)
        saveSets(context, sets)
        refreshSets(context,pageSets)
    }
}