package com.ozantok.combinia.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("combinia_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val ONBOARDING_SHOWN_KEY = "onboarding_shown"
    }

    fun setOnboardingShown(shown: Boolean) {
        prefs.edit().putBoolean(ONBOARDING_SHOWN_KEY, shown).apply()
    }

    fun isOnboardingShown(): Boolean {
        return prefs.getBoolean(ONBOARDING_SHOWN_KEY, false)
    }
}
