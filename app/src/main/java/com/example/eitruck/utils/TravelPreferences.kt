package com.example.eitruck.utils

import android.content.Context
import android.content.SharedPreferences

class TravelPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("travel_prefs", Context.MODE_PRIVATE)

    fun saveTravelIds(ids: Set<String>) {
        sharedPreferences.edit().putStringSet("travel_ids", ids).apply()
    }

    fun getTravelIds(): Set<String> {
        return sharedPreferences.getStringSet("travel_ids", emptySet()) ?: emptySet()
    }
}
