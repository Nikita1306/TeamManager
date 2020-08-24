package com.example.manager

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalmanager.playerdatabase.Player
import com.example.finalmanager.playerdatabase.PlayerDao

@Database(entities = [Player::class], version = 1)
abstract class PlayerDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}