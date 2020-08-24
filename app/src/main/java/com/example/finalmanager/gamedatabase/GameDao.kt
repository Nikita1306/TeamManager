package com.example.managerfootball.gamedatabase

import androidx.room.*
import com.example.finalmanager.gamedatabase.Game
@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAllGames(): List<Game>
    @Insert
    fun insertGame(game: Game)
    @Update
    fun update(game: Game)
    @Query("DELETE FROM game WHERE id = :gameId")
    fun delete(gameId: Int)
    @Query("UPDATE game SET id = id-1 WHERE id >=:gameId")
    fun updateId(gameId: Int)
    @Query("SELECT COUNT(isPlayed) FROM game WHERE isPlayed = 1")
    fun getGamesPlayed(): Int
    @Query("SELECT date FROM game")
    fun getGameByDate(): List<String>
}