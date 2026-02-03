package com.example.project_1.data

import androidx.lifecycle.LiveData

class AppRepository(private val userDao: UserDao) {
    val selectAll: LiveData<List<User>> = userDao.selectAll()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}
