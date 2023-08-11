package com.example.movieproject.ui.discover

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.service.MovieService
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveredViewModel @Inject constructor(
    private val movieService: MovieService
)
    : ViewModel() {


    private val _liveDataMovieList = MutableLiveData<MutableList<MovieDetail>>(mutableListOf())
    val liveDataMovieList: LiveData<MutableList<MovieDetail>> = _liveDataMovieList

    val liveDataLoading = MutableLiveData<Boolean>()

    private fun callDiscoveredRepos(page:Int, with_genres:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newMovieList = movieService.discover(BundleKeys.API_KEY, page, with_genres)
                val currentList = _liveDataMovieList.value ?: emptyList()
                val updatedList: MutableList<MovieDetail> = currentList.toMutableList().apply {
                    addAll(newMovieList.results)
                }
                _liveDataMovieList.postValue(updatedList)
                liveDataLoading.postValue(false)
            } catch (exception: Exception) {
            _liveDataMovieList.postValue(mutableListOf())
                // Handle exception
            }
        }

    }




    fun displayGroup(page:Int, with_genres:ArrayList<Int>) {
        val resultString = with_genres.joinToString(separator = ",")
        callDiscoveredRepos(page, resultString)
    }


}