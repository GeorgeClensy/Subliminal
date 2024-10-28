package com.geecee.subliminal.utils

import java.util.Calendar

//Returns morning, afternoon or evening depending on the time of day
fun getTimeOfDayGreeting(morning: String, afternoon: String, evening: String): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> morning
        in 12..17 -> afternoon
        else -> evening
    }
}