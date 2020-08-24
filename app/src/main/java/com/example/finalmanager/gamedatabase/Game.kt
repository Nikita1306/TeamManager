package com.example.finalmanager.gamedatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val firstTeam: String,
    @ColumnInfo
    val secondTeam:String,
    @ColumnInfo
    val scoreFirst: Int,
    @ColumnInfo
    val scoreSecond: Int,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val isPlayed: Boolean

)