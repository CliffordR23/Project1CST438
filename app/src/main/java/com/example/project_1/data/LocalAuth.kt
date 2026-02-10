package com.example.project_1

import android.content.Context
import org.json.JSONObject
import java.util.UUID

object LocalAuth {
    private const val PREFS = "local_auth"
    private const val KEY_USERS = "users"
    private const val KEY_CURRENT_USER_ID = "current_user_id"

    fun signUp(context: Context, email: String, password: String): String? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val usersJson = prefs.getString(KEY_USERS, "{}")
        val users = JSONObject(usersJson ?: "{}")

        val normalizedEmail = email.trim().lowercase()
        if (users.has(normalizedEmail)) {
            return null
        }

        val userId = UUID.randomUUID().toString()
        val entry = JSONObject()
        entry.put("password", password)
        entry.put("userId", userId)
        users.put(normalizedEmail, entry)

        prefs.edit()
            .putString(KEY_USERS, users.toString())
            .putString(KEY_CURRENT_USER_ID, userId)
            .apply()

        return userId
    }

    fun login(context: Context, email: String, password: String): String? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val usersJson = prefs.getString(KEY_USERS, "{}")
        val users = JSONObject(usersJson ?: "{}")
        val normalizedEmail = email.trim().lowercase()

        if (!users.has(normalizedEmail)) return null
        val entry = users.getJSONObject(normalizedEmail)
        val savedPass = entry.optString("password", null) ?: return null

        return if (savedPass == password) {
            val userId = entry.getString("userId")
            prefs.edit().putString(KEY_CURRENT_USER_ID, userId).apply()
            userId
        } else null
    }

    fun getCurrentUserId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENT_USER_ID, null)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getCurrentUserId(context) != null
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_CURRENT_USER_ID).apply()
    }

}
