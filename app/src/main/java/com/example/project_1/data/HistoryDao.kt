package com.example.project_1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM history WHERE userID = :userID")
    fun selectUserHistory(userID: Int): LiveData<List<History>>

    @Update
    suspend fun updateHistory(history: History)

    @Delete
    suspend fun deleteHistory(history: History)
}
