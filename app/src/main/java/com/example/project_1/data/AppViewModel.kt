package com.example.project_1.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application): AndroidViewModel(application) {
    private val repository: AppRepository

    init {
        val database = AppDatabase.getDatabase(application)

        repository = AppRepository(
            userDao = database.userDao(),
            historyDao = database.historyDao(),
            savedDao = database.savedDao()
        )
    }

//    USER
    fun insertUser(user:User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun selectUser(userID: Int): LiveData<User?> {
        return repository.selectUser(userID)
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

//    HISTORY
    fun insertHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHistory(history)
        }
    }

    fun selectUserHistory(userID: Int): LiveData<List<History>> {
        return repository.selectUserHistory(userID)
    }

    fun updateHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateHistory(history)
        }
    }

    fun deleteHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHistory(history)
        }
    }

//    SAVED
    fun insertSaved(saved: Saved) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSaved(saved)
        }
    }

    fun selectUserSaved(userID: Int): LiveData<List<Saved>> {
        return repository.selectUserSaved(userID)
    }

    fun updateSaved(saved: Saved) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSaved(saved)
        }
    }

    fun deleteSaved(saved: Saved) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSaved(saved)
        }
    }
}
