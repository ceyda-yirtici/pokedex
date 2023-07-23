package com.example.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "content") val content: String?,
)
