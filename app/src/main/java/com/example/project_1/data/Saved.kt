package com.example.project_1.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "saved", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = ["userID"],
        childColumns = ["userID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)
])
class Saved (
    @PrimaryKey(autoGenerate = false)
    val phoneNumber: Int,
    val userID: Int
)
