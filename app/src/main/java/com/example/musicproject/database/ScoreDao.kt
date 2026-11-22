package com.example.musicproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: GameScore)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScoreSync(score: GameScore)

    @Query("SELECT * FROM wynik WHERE typGry = :gameType ORDER BY timestamp DESC")
    fun getScoresForGameSync(gameType: String): List<GameScore>

    @Query("DELETE FROM wynik")
    fun deleteAll()
}
