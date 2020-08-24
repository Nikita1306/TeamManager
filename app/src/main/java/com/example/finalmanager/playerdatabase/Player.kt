package com.example.finalmanager.playerdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val firstName: String,
    @ColumnInfo
    val lastName:String,
    @ColumnInfo
    val position: String,
    @ColumnInfo
    val goals: Int,
    @ColumnInfo
    val assists: Int
)