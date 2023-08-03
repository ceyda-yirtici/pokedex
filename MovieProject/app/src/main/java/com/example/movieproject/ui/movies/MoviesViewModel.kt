package com.example.movieproject.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieGenre
import com.example.movieproject.model.MovieList
import com.example.movieproject.service.MovieService
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieService: MovieService)
    : ViewModel() {

    private val _liveDataMovieList = MutableLiveData<MovieList>()
    val liveDataMovieList: LiveData<MovieList> = _liveDataMovieList


    private val _liveDataGenreList = MutableLiveData<HashMap<Int, String>>()
    val liveDataGenreList: LiveData<HashMap<Int, String>> = _liveDataGenreList

    private val _liveDataPageNumber = MutableLiveData(1)

    init {
        _liveDataPageNumber.value?.let {
            callMovieRepos(it)
        }
        callGenreRepos()
    }
    fun setPageNumber(count:Int) {
        viewModelScope.launch {
            _liveDataPageNumber.value = count
        }
    }
    private fun callMovieRepos(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieList = try {
                movieService.getMovieList(BundleKeys.API_KEY, page)
            }
            catch  (exception: Exception) {
                ""
            }
            _liveDataMovieList.postValue(movieList as MovieList?)

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