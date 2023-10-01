package com.example.movieproject.ui.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.service.MovieService
import com.example.movieproject.service.PopularMoviesPagingSource
import com.example.movieproject.service.SearchPagingSource
import com.example.movieproject.ui.GenreMapper
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
class MoviesViewModel  @Inject constructor(
    private val movieService: MovieService, application: Application
)
    : ViewModel() {

    private val movieDao: MovieDao

    data class PopularMoviesUiState(

        var movieList: Flow<PagingData<MovieDetail>> = flowOf(),
        val genreMapper: HashMap<Int,String> = GenreMapper.genreMapper,
        val favoritesList: MutableList<Int> = mutableListOf(),
        var viewType: Int = 1,
        val loading: Boolean = true,
        var searchText: String = "",
        var searching: Boolean = false

        )


    private val _uiState = MutableStateFlow(PopularMoviesUiState())
    val uiState: StateFlow<PopularMoviesUiState> = _uiState.asStateFlow()



    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        movieDao = database.movieDao()
        callMovieRepos()
        callGenreRepos()
    }



    private fun getAllMovies(): Flow<PagingData<MovieDetail>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            PopularMoviesPagingSource(movieService, uiState.value.favoritesList)
        }.flow.cachedIn(viewModelScope)
    }

    private fun getAllSearchValues(): Flow<PagingData<MovieDetail>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            SearchPagingSource(movieService, uiState.value.favoritesList, uiState.value.searchText)
        }.flow.cachedIn(viewModelScope)
    }

    private fun callMovieRepos() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedList:Flow<PagingData<MovieDetail>> = if (!uiState.value.searching)
                    getAllMovies()
                else
                    getAllSearchValues()

                _uiState.update {
                    it.copy(movieList = updatedList)
                }
            } catch (exception: Exception) {
                // Handle exception
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(favoritesList = movieDao.getAllByIds() as MutableList<Int>, loading = false)
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
               _uiState.update {
                   it.copy(genreMapper = genreMap)
               }
            }
            catch  (exception: Exception) {
                Log.e("call", "genreRepos")
            }
        }
    }

    fun displayGroup() {
        callMovieRepos()
    }

    fun getMovieDao(): MovieDao {
        return movieDao
    }


}