package com.example.movieproject.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _liveDataPageNumber = MutableLiveData(1)
    val liveDataPageNumber: LiveData<Int> = _liveDataPageNumber

    init {
        _liveDataPageNumber.value?.let { callMovieRepos(it) }
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

    fun displayGroup(page:Int) {
            callMovieRepos(page)
    }
}