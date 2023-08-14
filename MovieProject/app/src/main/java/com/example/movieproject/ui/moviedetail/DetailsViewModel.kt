package com.example.movieproject.ui.moviedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieList
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.service.MovieService
import com.example.movieproject.utils.BundleKeys
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieService: MovieService, application: Application
)
    : ViewModel() {

    private val movieDao: MovieDao


    private val _liveDataMovie = MutableLiveData<MovieDetail>()
    val liveDataMovie: LiveData<MovieDetail> = _liveDataMovie


    init {
        val database = AppDatabaseProvider.getAppDatabase(application)

        movieDao = database.movieDao()
    }
    fun getMovieDao(): MovieDao {
        return movieDao
    }
    private fun callMovieRepos(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movie = try {
                movieService.getMovie(id,BundleKeys.API_KEY)
            }
            catch  (exception: Exception) {
                ""
            }
            _liveDataMovie.postValue(movie as MovieDetail?)
        }
    }
    fun displayMovie(id:Int) {
            callMovieRepos(id)
    }


}