package com.example.musicproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Table and column names are kept from the first (Polish) schema,
// so existing user data keeps working without a Room migration.
@Entity(tableName = "wynik")
data class GameScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "typGry") val gameType: String, // one of GameTypes
    @ColumnInfo(name = "punkty") val points: Int,      // correctCount - incorrectCount
    @ColumnInfo(name = "poprawne") val correctCount: Int,
    @ColumnInfo(name = "bledne") val incorrectCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)
