package com.example.movieproject.ui.discover

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.service.DiscoveredPagingSource
import com.example.movieproject.service.MovieService
import com.example.movieproject.service.PopularMoviesPagingSource
import com.example.movieproject.utils.BundleKeys
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveredViewModel  @Inject constructor(
    private val movieService: MovieService,  application: Application
)
    : ViewModel() {


    private val movieDao: MovieDao

    private val _uiState = MutableStateFlow(DiscoveredMoviesUiState())
    val uiState: StateFlow<DiscoveredMoviesUiState> = _uiState.asStateFlow()


    data class DiscoveredMoviesUiState(

        var movieList: Flow<PagingData<MovieDetail>> = flowOf(),
        val favoritesList: MutableList<Int> = mutableListOf(),
        val loading: Boolean = true,
    )

    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        movieDao = database.movieDao()


    }
    fun getMovieDao(): MovieDao {
        return movieDao
    }


    private fun getAllMovies(with_genres: String, min_vote: Float): Flow<PagingData<MovieDetail>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            DiscoveredPagingSource(movieService, uiState.value.favoritesList, with_genres, min_vote)
        }.flow.cachedIn(viewModelScope)
    }

    private fun callDiscoveredRepos(with_genres: String, min_vote: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedList:Flow<PagingData<MovieDetail>> =   getAllMovies(with_genres, min_vote)
                _uiState.update { it.copy(movieList = updatedList ,
                    loading = false) }


            } catch (exception: Exception) {

            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(favoritesList = movieDao.getAllByIds() as MutableList<Int>, loading = false)
            }
        }
    }



    fun displayGroup(with_genres:String, min_vote : Float) {
        callDiscoveredRepos(with_genres, min_vote)
    }


}