package com.example.project_1.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AuthManager {
    private const val PREFS = "auth_prefs"
    private const val KEY_CURRENT_USER_ID = "current_user_id"

    suspend fun signUp(context: Context, email: String, password: String): Int? =
        withContext(Dispatchers.IO) {

            val db = AppDatabase.getDatabase(context)
            val userDao = db.userDao()

            val normalizedEmail = email.trim().lowercase()
            val existing = userDao.getUserByEmail(normalizedEmail)
            if (existing != null) return@withContext null

            val user = User(
                userID = 0,              // auto-generate
                username = "",           // empty (unused)
                email = normalizedEmail,
                password = password
            )

            val rowId = userDao.insertUser(user)
            if (rowId <= 0) return@withContext null

            val newUserId = rowId.toInt()

            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_CURRENT_USER_ID, newUserId)
                .apply()

            return@withContext newUserId
        }


    suspend fun login(context: Context, email: String, password: String): Int? = withContext(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(context)
        val userDao = db.userDao()

        val normalizedEmail = email.trim().lowercase()
        val user = userDao.getUserByEmail(normalizedEmail) ?: return@withContext null
        if (user.password != password) return@withContext null

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_CURRENT_USER_ID, user.userID)
            .apply()

        return@withContext user.userID
    }

    fun getCurrentUserId(context: Context): Int {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_CURRENT_USER_ID, -1)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getCurrentUserId(context) != -1
    }

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_CURRENT_USER_ID)
            .apply()
    }
}
