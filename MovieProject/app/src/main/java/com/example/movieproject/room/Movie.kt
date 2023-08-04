package com.example.movieproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "movie_id") val movie_id: Int,
)
