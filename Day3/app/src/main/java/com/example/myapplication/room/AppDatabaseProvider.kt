package com.example.myapplication.room

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {

    fun getAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "appdb").build()
    }

}