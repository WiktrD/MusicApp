package com.example.musicproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScoreSync(score: GameScore)

    // Room runs the query in the background and re-emits when the table changes
    @Query("SELECT * FROM wynik WHERE typGry = :gameType ORDER BY timestamp DESC")
    fun observeScores(gameType: String): LiveData<List<GameScore>>

    @Query("DELETE FROM wynik")
    fun deleteAll()
}
