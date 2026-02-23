package com.example.project_1.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "history", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = ["userID"],
        childColumns = ["userID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)
])
class History (
    @PrimaryKey(autoGenerate = true)
    val historyID: Int,
    val userID: Int,
    val phoneNumber: Int,
    val data: String,
    val time: String
)
