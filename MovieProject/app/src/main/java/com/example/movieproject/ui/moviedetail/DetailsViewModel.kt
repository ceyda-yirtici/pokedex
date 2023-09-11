package com.example.movieproject.ui.moviedetail

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
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.service.RecPagingSource
import com.example.movieproject.service.MovieService
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
class DetailsViewModel @Inject constructor(
    private val movieService: MovieService, application: Application
)
    : ViewModel() {

    private val movieDao: MovieDao

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    data class MovieUiState(

        val movieList: Flow<PagingData<MovieDetail>> = flowOf(),
        val movie: MovieDetail? = null,
        val castList: ArrayList<CastPerson> = arrayListOf(),
        val loading: Boolean = true,
    )


    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        movieDao = database.movieDao()
    }

    fun getMovieDao() : MovieDao {
        return movieDao
    }
    private fun callMovieRepos(id:Int) {


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movie =  movieService.getMovie(id,BundleKeys.API_KEY)

                _uiState.update {
                    it.copy(movie = movie,loading = false)
                }
            }
            catch  (exception: Exception) {
                _uiState.update {
                    it.copy(movie = null)
                }
            }
        }
    }
    private fun callCastRepos(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credit = movieService.getCredit(id, BundleKeys.API_KEY)
                _uiState.update {
                    it.copy(castList = credit.cast)
                }
            } catch (exception: Exception) {
                Log.e("call cast", "Exception: ${exception.message}")
            }
        }
    }


    fun getAllMovies(id: Int): Flow<PagingData<MovieDetail>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            RecPagingSource(movieService, id)
        }.flow.cachedIn(viewModelScope)
    }

    private fun callRecRepos(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val updatedList = getAllMovies(id)

                _uiState.update {
                    it.copy(movieList = updatedList)
                }
            } catch (exception: Exception) {
                // Handle exception
            }
        }
    }



    fun displayRecs(id:Int) {
        callRecRepos(id)
    }
    fun displayMovie(id:Int) {
            callMovieRepos(id)
    }
    fun displayCast(id:Int) {
            callCastRepos(id)
    }


}

