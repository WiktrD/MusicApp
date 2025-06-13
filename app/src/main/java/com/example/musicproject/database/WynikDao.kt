package com.example.musicproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WynikDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWynik(wynik: Wynik)

    @Query("SELECT * FROM wynik WHERE typGry = :typGry ORDER BY timestamp DESC")
    suspend fun getWynikiDlaGry(typGry: String): List<Wynik>

    @Query("SELECT * FROM wynik WHERE typGry = :typGry ORDER BY timestamp DESC")
    fun getWynikiDlaGrySync(typGry: String): List<Wynik>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWynikSync(wynik: Wynik)

    @Query("DELETE FROM wynik")
    suspend fun clearAll()

    // Dodaj to:
    @Query("DELETE FROM wynik")
    fun deleteAll()
}
