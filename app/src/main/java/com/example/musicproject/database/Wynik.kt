package com.example.musicproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wynik")
data class Wynik(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val typGry: String,      // np. "pianino", "pieciolinia", "quiz"
    val punkty: Int,
    val poprawne: Int,
    val bledne: Int,
    val timestamp: Long = System.currentTimeMillis()
)
