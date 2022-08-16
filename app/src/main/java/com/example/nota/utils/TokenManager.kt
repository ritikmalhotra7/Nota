package com.example.nota.utils

import android.content.Context
import com.example.nota.utils.Constants.PREFS_TOKEN_FILE
import com.example.nota.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context:Context) {
    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().apply {
            putString(USER_TOKEN, token)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN,null)
    }
}