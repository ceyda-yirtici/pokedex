package com.example.movieproject.ui

import android.widget.ImageButton
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.Movie
import com.example.myapplication.room.MovieDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class FavoritesManager private constructor(
    private val movieDao: MovieDao?,
private val viewModelScope: CoroutineScope) {



    private var heartResource: Int = R.drawable.heart_shape_grey
    companion object {
        @Volatile
        private var instance: FavoritesManager? = null

        fun getInstance(movieDao: MovieDao, viewModelScope: CoroutineScope): FavoritesManager {
            return instance ?: synchronized(this) {
                instance ?: FavoritesManager(movieDao, viewModelScope).also { instance = it }
            }
        }
    }
    fun addMovieToDB(
        clickedMovie: MovieDetail,
        heartButton: ImageButton?
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newMovie = Movie(movie_id = clickedMovie.id)
                movieDao?.insert(newMovie)
            } catch (e: Exception) {
                clickedMovie.heart_tag = "outline"
            }
        }
     }


    fun removeMovieFromDB(
        clickedMovie: MovieDetail,
        heartButton: ImageButton?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieDao?.delete(movieDao.get(clickedMovie.id))

            } catch (e: Exception) {
                clickedMovie.heart_tag = "filled"
            }
        }

    }

}