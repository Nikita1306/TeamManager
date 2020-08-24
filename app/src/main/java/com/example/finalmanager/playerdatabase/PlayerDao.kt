package com.example.finalmanager.playerdatabase

import androidx.room.*
import com.example.finalmanager.playerdatabase.Player

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player")
    fun getAllPlayers(): List<Player>
    @Insert
    fun insertPlayer(player: Player)
    @Update
    fun update(player: Player)
    @Query("DELETE FROM player WHERE id = :playerId")
    fun delete(playerId: Int)
    @Query("UPDATE player SET id = id-1 WHERE id >=:playerId")
    fun updateId(playerId: Int)
}