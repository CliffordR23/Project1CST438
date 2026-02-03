package com.example.project_1.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application): AndroidViewModel(application) {
    private val selectAll: LiveData<List<User>>
    private val repository: AppRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = AppRepository(userDao)
        selectAll = repository.selectAll
    }

    fun insertUser(user:User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }
}
