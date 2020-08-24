package com.example.finalmanager.gamedatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.managerfootball.gamedatabase.GameDao

@Database(entities = [Game::class], version = 1)
abstract class GameDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
}