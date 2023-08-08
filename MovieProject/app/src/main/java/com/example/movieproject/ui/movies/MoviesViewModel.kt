package com.example.movieproject.ui.movies

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieproject.MovieApplication
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieGenre
import com.example.movieproject.model.MovieList
import com.example.movieproject.room.AppDatabase
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.room.Movie
import com.example.movieproject.service.MovieService
import com.example.movieproject.utils.BundleKeys
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieService: MovieService, application: Application
)
    : ViewModel() {

    private val movieDao: MovieDao

    private val _liveDataMovieList = MutableLiveData<MutableList<MovieDetail>>()
    val liveDataMovieList: LiveData<MutableList<MovieDetail>> = _liveDataMovieList

    private val _liveDataViewType = MutableLiveData<Boolean>()
    val liveDataViewType: LiveData<Boolean> = _liveDataViewType

    val liveDataLoading = MutableLiveData<Boolean>()

    private val _liveDataLikedMovieIds = MutableLiveData<List<Int>>()
    val liveDataLikedMovieIds: LiveData<List<Int>> = _liveDataLikedMovieIds


    private val _liveDataGenreList = MutableLiveData<HashMap<Int, String>>()
    val liveDataGenreList: LiveData<HashMap<Int, String>> = _liveDataGenreList

    private val _liveDataPageNumber = MutableLiveData(1)

    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        movieDao = database.movieDao()
        viewModelScope.launch(Dispatchers.IO) {
            _liveDataLikedMovieIds.postValue(movieDao.getAllByIds())
        }
        _liveDataPageNumber.value?.let {
            callMovieRepos(it)
        }
        callGenreRepos()
    }
    fun getMovieDao(): MovieDao {
        return movieDao
    }



    private fun getLikedMovieIds(): List<Int> {
        // Query your Room database to get a list of liked movie IDs
        return movieDao.getAllByIds()
    }
    fun updateLikedMovieIds() {
        viewModelScope.launch(Dispatchers.IO) {
            _liveDataLikedMovieIds.postValue(getLikedMovieIds() as List<Int>?)
        }
    }

    fun setPageNumber(count:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _liveDataPageNumber.postValue(count)
        }
    }
    private fun callMovieRepos(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newMovieList = movieService.getMovieList(BundleKeys.API_KEY, page)
                val currentList = _liveDataMovieList.value ?: emptyList()
                val updatedList: MutableList<MovieDetail> = currentList.toMutableList().apply {
                    addAll(newMovieList.results)
                }

                _liveDataMovieList.postValue(updatedList)
                liveDataLoading.postValue(false)
                _liveDataViewType.postValue(true)
            } catch (exception: Exception) {
                // Handle exception
            }
        }
    }

    private fun callGenreRepos() {
        viewModelScope.launch(Dispatchers.IO) {
           try {
               val genreList: GenreList = movieService.getMovieGenres(BundleKeys.API_KEY)
                val genreMap = HashMap<Int, String>()
                for (genre in genreList.genres) {
                    genreMap[genre.id] = genre.genre_name
                }
               _liveDataGenreList.postValue(genreMap)
            }
            catch  (exception: Exception) {
                Log.e("call", "genreRepos")
                _liveDataGenreList.postValue(HashMap())
            }
        }
    }

    fun displayGroup(page:Int) {
            callMovieRepos(page)
    }



}