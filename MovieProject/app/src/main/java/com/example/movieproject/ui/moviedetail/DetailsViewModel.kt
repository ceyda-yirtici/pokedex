package com.example.movieproject.ui.moviedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieCredit
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

    val liveDataLoading = MutableLiveData<Boolean>()

    private val _liveDataCast = MutableLiveData<MutableList<CastPerson>>(mutableListOf())
    val liveDataCast: LiveData<MutableList<CastPerson>> = _liveDataCast

    private val _liveDataMovieList = MutableLiveData<MutableList<MovieDetail>>(mutableListOf())
    val liveDataMovieList: LiveData<MutableList<MovieDetail>> = _liveDataMovieList



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
            liveDataLoading.postValue(false)
            _liveDataMovie.postValue(movie as MovieDetail?)
        }
    }
    private fun callCastRepos(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credit = movieService.getCredit(id, BundleKeys.API_KEY)
                _liveDataCast.postValue(credit.cast as MutableList<CastPerson>?)
            } catch (exception: Exception) {
                Log.e("call cast", "Exception: ${exception.message}")
            }
        }
    }


    private fun callRecRepos(id: Int, page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newMovieList = movieService.getRecommendations(id, BundleKeys.API_KEY, page)
                val currentList = _liveDataMovieList.value ?: emptyList()
                val updatedList: MutableList<MovieDetail> = currentList.toMutableList().apply {
                    addAll(newMovieList.results)
                }

                _liveDataMovieList.postValue(updatedList)
            } catch (exception: Exception) {
                // Handle exception
            }
        }
    }



    fun displayRecs(id:Int, page:Int) {
        callRecRepos(id, page)
    }
    fun displayMovie(id:Int) {
            callMovieRepos(id)
    }
    fun displayCast(id:Int) {
            callCastRepos(id)
    }


}