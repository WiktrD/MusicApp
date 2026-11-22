package com.example.musicproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameScore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        // Legacy (Polish) file name, kept so existing installs do not lose their scores.
        private const val DATABASE_NAME = "wyniki.db"

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}
