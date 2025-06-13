package com.example.musicproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Wynik::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wynikDao(): WynikDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wyniki.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
