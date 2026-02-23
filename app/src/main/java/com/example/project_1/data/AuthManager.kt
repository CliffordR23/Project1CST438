package com.example.project_1.data

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AuthManager {
    private const val PREFS = "auth_prefs"
    private const val KEY_CURRENT_USER_ID = "current_user_id"

    // must be called with a coroutine
    suspend fun signUp(context: Context, email: String, password: String): Int? =
        withContext(Dispatchers.IO) {

            val db = AppDatabase.getDatabase(context)
            val userDao = db.userDao()

            val normalizedEmail = email.trim().lowercase()
            val existing = userDao.getUserByEmail(normalizedEmail)
            if (existing != null) return@withContext null

            val user = User(
                userID = 0,              // auto-generate
                email = normalizedEmail,
                password = password
            )

            val rowId = userDao.insertUser(user)
            if (rowId <= 0) return@withContext null

            val newUserId = rowId.toInt()

            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
                putInt(KEY_CURRENT_USER_ID, newUserId)
            }

            return@withContext newUserId
        }

    suspend fun login(context: Context, email: String, password: String): Int? =
        withContext(Dispatchers.IO) {

            val db = AppDatabase.getDatabase(context)
            val userDao = db.userDao()

            // finds user
            val normalizedEmail = email.trim().lowercase()
            val user = userDao.getUserByEmail(normalizedEmail) ?: return@withContext null
            if (user.password != password) return@withContext null

            // remembers log in
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
                putInt(KEY_CURRENT_USER_ID, user.userID)
            }

            return@withContext user.userID
        }

    suspend fun loginOrCreateFromGoogle(context: Context, email: String): Int =
        withContext(Dispatchers.IO) {

            val db = AppDatabase.getDatabase(context)
            val userDao = db.userDao()
            val normalizedEmail = email.trim().lowercase()

            // checks if email is already in the database or else fails
            val existing = userDao.getUserByEmail(normalizedEmail)
            val userId = if (existing != null) {
                existing.userID
            } else {
                // inserts into db
                val rowId = userDao.insertUser(
                    User(
                        userID = 0,
                        password = "",
                        email = normalizedEmail
                    )
                )
                rowId.toInt()
            }

            // remember me
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
                putInt(KEY_CURRENT_USER_ID, userId)
            }

            userId
        }

    // returns user ID
    fun getCurrentUserId(context: Context): Int {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_CURRENT_USER_ID, -1)
    }

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
            remove(KEY_CURRENT_USER_ID)
        }
    }
}
