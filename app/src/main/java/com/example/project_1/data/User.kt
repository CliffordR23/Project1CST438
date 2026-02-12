package com.example.project_1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    val userID: Int,
    val password: String,
    val email: String
)
