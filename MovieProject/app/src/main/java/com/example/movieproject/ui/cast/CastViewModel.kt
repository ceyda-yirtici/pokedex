package com.example.movieproject.ui.cast

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.service.MovieService
import com.example.movieproject.ui.MovieRecyclerAdapter
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CastViewModel @Inject constructor(
    private val movieService: MovieService,
    application: Application
)
    : ViewModel() {

    private val _uiState = MutableStateFlow(CastUiState())
    val uiState: StateFlow<CastUiState> = _uiState.asStateFlow()

    //val genreMapper =  MovieRecyclerAdapter.genreMapper

    data class CastUiState(

        val movieList: ArrayList<MovieDetail> = arrayListOf(),
        val backDropMovie: MovieDetail? = null,
        val cast: CastPerson? = null,
        val loading: Boolean = true,
        //val genreNames: ArrayList<String> = arrayListOf()
    )


    private fun callMovieRepos(id: Int) {


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credit = movieService.getPersonMovieCredits(id, BundleKeys.API_KEY)
                val currentList = credit.castMovies

                val highestVotedMovie = currentList
                    .filter { it.backdrop_path != null }
                    .maxBy { it.vote }
                _uiState.update {
                    it.copy(movieList = currentList,backDropMovie = highestVotedMovie)
                }
            /*
                val genres = arrayListOf<String>()
                for (movie in currentList) {

                    for (genreId in movie.genre_ids) {
                        val genreName = MovieRecyclerAdapter.genreMapper[genreId]
                        if (genreName != null) {
                            genres.add(genreName)
                        }
                    }
                }
                _uiState.update {
                    it.copy(genreNames = genres)
                }*/

            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(movieList = arrayListOf())
                }
            }
        }
    }
    private fun callCastRepos(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val person = movieService.getPerson(id, BundleKeys.API_KEY)

                _uiState.update { it.copy(loading = false, cast = person) }
            } catch (exception: Exception) {
                Log.e("call cast", "Exception: ${exception.message}")
            }
        }
    }

    fun displayCast(id: Int) {
        callCastRepos(id)
        callMovieRepos(id)
    }





}