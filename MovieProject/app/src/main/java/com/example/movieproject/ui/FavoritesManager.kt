package com.example.movieproject.ui

import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.room.Movie
import com.example.myapplication.room.MovieDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class FavoritesManager private constructor(private val movieDao: MovieDao) {

    private var heartResource: Int = R.drawable.heart_shape_grey


    companion object {
        @Volatile
        private var instance: FavoritesManager? = null

        fun getInstance(movieDao: MovieDao): FavoritesManager {
            return instance ?: synchronized(this) {
                instance ?: FavoritesManager(movieDao).also { instance = it }
            }
        }
    }
    fun addMovieToDB(
        clickedMovie: MovieDetail,
        heartButton: ImageButton
    ) {
            try {
                val newMovie = Movie(movie_id = clickedMovie.id)
                movieDao.insert(newMovie)
            }
             catch (e: Exception) {
                heartButton.setImageResource(heartResource)
                clickedMovie.heart_tag = "outline"
            }
        }


    fun removeMovieFromDB(
        clickedMovie: MovieDetail,
        heartButton: ImageButton
    ) {
            try {
                movieDao.delete(movieDao.get(clickedMovie.id))

            } catch (e: Exception) {
                heartButton.setImageResource(R.drawable.heart_shape_red)
                clickedMovie.heart_tag = "filled"
            }

    }

}