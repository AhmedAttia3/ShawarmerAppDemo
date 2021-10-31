package com.shawarmer.app.data.storage.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private const val SHARED_NAME = "shawarmer"
        private const val LANGUAGE = "LANGUAGE"

    }

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    var language: String?
        get() {
            return sharedPreferences.getString(LANGUAGE, "en")
        }
        set(value) = sharedPreferences.edit().putString(LANGUAGE, value).apply()


}