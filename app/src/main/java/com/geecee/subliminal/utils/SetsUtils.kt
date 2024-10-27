package com.geecee.subliminal.utils

import java.util.Date

data class Set(
    var title: String,
    var cards: List<Card>,
    var lastRevised: Date? = null
)