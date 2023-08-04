package com.example.movieproject.room

import android.content.Context
import androidx.room.Room
import com.example.movieproject.room.AppDatabase

object AppDatabaseProvider {

    private var database: AppDatabase? = null
    fun getAppDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: buildDatabase(context).also { database = it }
        }
    }

    private fun buildDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "appdb"
        ).build()
    }

}