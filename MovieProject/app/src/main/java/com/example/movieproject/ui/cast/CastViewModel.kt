package com.example.movieproject.ui.cast

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
class CastViewModel @Inject constructor(
    private val movieService: MovieService, application: Application
)
    : ViewModel() {

    private val _liveDataMovieList = MutableLiveData<MutableList<MovieDetail>>(mutableListOf())
    val liveDataMovieList: LiveData<MutableList<MovieDetail>> = _liveDataMovieList

    private val _liveDataBackDropMovie = MutableLiveData<MovieDetail>()
    val liveDataBackDropMovie: MutableLiveData<MovieDetail> = _liveDataBackDropMovie


    val liveDataLoading = MutableLiveData<Boolean>()

    private val _liveDataCast = MutableLiveData<CastPerson>()
    val liveDataCast: LiveData<CastPerson> = _liveDataCast


    private fun callMovieRepos(id:Int) {


        viewModelScope.launch(Dispatchers.IO) {
             try {
                 val credit =movieService.getPersonMovieCredits(id,BundleKeys.API_KEY)
                 val currentList = credit.castMovies
                 val highestVotedMovie = currentList
                     .filter { it.backdrop_path != null }
                     .maxBy { it.popularity }

                 _liveDataBackDropMovie.postValue(highestVotedMovie)
                 _liveDataMovieList.postValue(currentList)

            }
            catch  (exception: Exception) {
                _liveDataMovieList.postValue(mutableListOf())
            }
        }
    }
    private fun callCastRepos(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val person = try {
                 movieService.getPerson(id, BundleKeys.API_KEY)
            } catch (exception: Exception) {
                Log.e("call cast", "Exception: ${exception.message}")
            }
            liveDataLoading.postValue(false)
            _liveDataCast.postValue(person as CastPerson?)
        }
    }






    fun displayMovie(id:Int) {
        callMovieRepos(id)
    }
    fun displayCast(id:Int) {
        callCastRepos(id)
    }


}