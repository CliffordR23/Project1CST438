package com.example.project_1.data

import androidx.lifecycle.LiveData

class AppRepository(
    private val userDao: UserDao,
    private val historyDao: HistoryDao,
    private val savedDao: SavedDao
) {
//    USER
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun selectUser(userID: Int): LiveData<User?> {
        return userDao.selectUser(userID)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

//    HISTORY
    suspend fun insertHistory(history: History) {
        historyDao.insertHistory(history)
    }

    fun selectUserHistory(userID: Int): LiveData<List<History>> {
        return historyDao.selectUserHistory(userID)
    }

    suspend fun updateHistory(history: History) {
        historyDao.updateHistory(history)
    }

    suspend fun deleteHistory(history: History) {
        historyDao.deleteHistory(history)
    }

//    SAVED
    suspend fun insertSaved(saved: Saved) {
        savedDao.insertSaved(saved)
    }

    fun selectUserSaved(userID: Int): LiveData<List<Saved>> {
        return savedDao.selectUserSaved(userID)
    }

    suspend fun updateSaved(saved: Saved) {
        savedDao.updateSaved(saved)
    }

    suspend fun deleteSaved(saved: Saved) {
        savedDao.deleteSaved(saved)
    }
}
