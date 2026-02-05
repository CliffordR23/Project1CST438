package com.example.project_1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SavedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSaved(saved: Saved)

    @Query("SELECT * FROM saved WHERE userID = :userID")
    fun selectUserSaved(userID: Int): LiveData<List<Saved>>

    @Update
    suspend fun updateSaved(saved: Saved)

    @Delete
    suspend fun deleteSaved(saved: Saved)
}
