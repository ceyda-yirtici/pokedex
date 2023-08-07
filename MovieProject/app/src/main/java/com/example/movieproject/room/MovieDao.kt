package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movieproject.room.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table")
    fun getAll(): List<Movie>


    @Query("SELECT movie_id FROM movie_table")
    fun getAllByIds(): List<Int>

    @Query("SELECT * FROM movie_table WHERE movie_id = :movieId")
    fun get(movieId:Int): Movie


    @Query("SELECT * FROM movie_table WHERE movie_id IN (:movieIds)")
    suspend fun loadAllByIds(movieIds: IntArray): List<com.example.movieproject.room.Movie>

    @Query("SELECT COUNT(*) FROM movie_table WHERE movie_id = :movieId")
    fun getCountById(movieId: Int): Int

    @Query("SELECT COUNT(*) FROM movie_table")
    fun getCount(): Int

    @Insert
    fun insert(movie: Movie)

    @Insert
    fun insertAll(movies: List<com.example.movieproject.room.Movie>)

    @Delete
    fun delete(movie: com.example.movieproject.room.Movie)

    @Query("DELETE FROM movie_table")
     fun deleteAllMovies()




}