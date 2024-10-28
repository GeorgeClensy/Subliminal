@file:Suppress("unused")

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
    var cards: List<Card> = listOf() // Store cards directly in the set
)

data class Card(
    var content: String,
    var contentAnswer: String? = null
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

fun duplicateSet(context: Context, title: String, newTitle: String? = null, pageSets: MutableState<List<Set>>) {
    val sets = loadSets(context).toMutableList()

    val setToDuplicate = sets.find { it.title == title }
    setToDuplicate?.let { originalSet ->
        // Create a duplicate with a new title
        val duplicatedSet = originalSet.copy(
            title = newTitle ?: "${originalSet.title} (Copy)" // If new title is null then generate a name for me
        )

        sets.add(duplicatedSet)
        saveSets(context, sets)
        refreshSets(context,pageSets)
    }
}

//Cards

fun getCardsInSet(context: Context, setTitle: String): List<Card> {
    val sets = loadSets(context)
    val set = sets.find { it.title == setTitle }
    return set?.cards ?: emptyList() // Return the list of cards, or an empty list if not found
}


fun addCardToSet(context: Context, setTitle: String, content: String, contentAnswer: String? = null) {
    val sets = loadSets(context).toMutableList()
    val set = sets.find { it.title == setTitle }

    set?.let {
        val newCard = Card(content = content, contentAnswer = contentAnswer)
        it.cards += newCard // Add the new card to the existing list
        saveSets(context, sets)
    }
}

fun editCardInSet(context: Context, setTitle: String, cardContent: String, newContent: String, newAnswer: String?) {
    val sets = loadSets(context).toMutableList()
    val set = sets.find { it.title == setTitle }

    set?.let {
        val cardToEdit = it.cards.find { card -> card.content == cardContent }
        cardToEdit?.let { card ->
            card.content = newContent
            card.contentAnswer = newAnswer
            saveSets(context, sets)
        }
    }
}

fun duplicateCardInSet(context: Context, setTitle: String, cardContent: String) {
    val sets = loadSets(context).toMutableList()
    val set = sets.find { it.title == setTitle }

    set?.let {
        val cardToDuplicate = it.cards.find { card -> card.content == cardContent }
        cardToDuplicate?.let { card ->
            val duplicatedCard = card.copy(content = "${card.content} (Copy)")
            it.cards += duplicatedCard // Add the duplicated card
            saveSets(context, sets)
        }
    }
}

fun deleteCardFromSet(context: Context, setTitle: String, cardContent: String) {
    val sets = loadSets(context).toMutableList()
    val set = sets.find { it.title == setTitle }

    set?.let {
        it.cards = it.cards.filter { card -> card.content != cardContent } // Remove the card
        saveSets(context, sets)
    }
}

