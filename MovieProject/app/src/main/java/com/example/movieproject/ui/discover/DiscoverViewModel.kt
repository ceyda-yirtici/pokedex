package com.example.movieproject.ui.discover

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.service.MovieService
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DiscoverViewModel  @Inject constructor(
    private val movieService: MovieService
)
    : ViewModel() {


    private val _uiState = MutableStateFlow(DiscoverMoviesUiState())
    val uiState: StateFlow<DiscoverMoviesUiState> = _uiState.asStateFlow()

    data class DiscoverMoviesUiState(

        var genreOptionList: List<String> = listOf(),
    )

    private val _liveDataGenreList = MutableLiveData<HashMap<Int, String>>()
    val liveDataGenreList: LiveData<HashMap<Int, String>> = _liveDataGenreList

    private val _genreOptionList = MutableLiveData<MutableList<String>>()
    val genreOptionList: LiveData<MutableList<String>> = _genreOptionList

    init {
        callGenreRepos()
    }

    private fun callGenreRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val genreList: GenreList = movieService.getMovieGenres(BundleKeys.API_KEY)
                val genreNames : MutableList<String> = mutableListOf()
                val genreMap = HashMap<Int, String>()
                for (genre in genreList.genres) {
                    genreNames.add(genre.genre_name)
                    genreMap[genre.id] = genre.genre_name
                }
                _liveDataGenreList.postValue(genreMap)
                _genreOptionList.postValue(genreNames)
            }
            catch  (exception: Exception) {
                Log.e("call", "genreRepos")
                _liveDataGenreList.postValue(HashMap())
            }
        }
    }

}