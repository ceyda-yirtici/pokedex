package com.example.movieproject.ui.favorites

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.service.MovieService
import com.example.movieproject.ui.movies.MoviesViewModel
import com.example.movieproject.utils.BundleKeys
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val movieService: MovieService, application: Application
)
    : ViewModel() {

    private val movieDao: MovieDao

    private val _uiState = MutableStateFlow(FavoriteMoviesUiState())
    val uiState: StateFlow<FavoriteMoviesUiState> = _uiState.asStateFlow()


    data class FavoriteMoviesUiState(

        val movieList: ArrayList<MovieDetail> = arrayListOf(),
        val loading: Boolean = true,
        )

    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        movieDao = database.movieDao()
        callMovieRepos()


    }
    fun getMovieDao(): MovieDao {
        return movieDao
    }

    private fun callMovieRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieList = movieDao.getAllByIds().map { movieId ->
                    movieService.getMovie(movieId, BundleKeys.API_KEY)
                }
                movieList.map { it.heart_tag = "filled" }
                val currentList = arrayListOf<MovieDetail>()
                val updatedList: MutableList<MovieDetail> = currentList.toMutableList().apply {
                    addAll(movieList)
                }
                _uiState.update { it.copy(movieList = updatedList as ArrayList<MovieDetail>,
                loading = false) }


            } catch (exception: Exception) {

            }
        }
    }
    fun displayGroup(){
        callMovieRepos()
    }







}





