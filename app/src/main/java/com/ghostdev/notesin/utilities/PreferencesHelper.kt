package com.ghostdev.notesin.utilities

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREF_NAME = "note_prefs"
    private const val KEY_FIRST_TIME = "is_first_time"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isFirstTime(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_FIRST_TIME, true)
    }

    fun setFirstTime(context: Context, isFirstTime: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_FIRST_TIME, isFirstTime)
        editor.apply()
    }
}