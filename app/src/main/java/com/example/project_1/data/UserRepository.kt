package com.example.project_1.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    val selectAll: LiveData<List<User>> = userDao.selectAll()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}
