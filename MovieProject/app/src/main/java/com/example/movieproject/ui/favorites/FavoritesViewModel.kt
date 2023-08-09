package com.example.movieproject.ui.favorites

import android.app.Application
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
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val movieService: MovieService, application: Application
)
    : ViewModel() {

    private val movieDao: MovieDao

    val liveDataLoading = MutableLiveData<Boolean>()

    private val _liveDataFavoritesList = MutableLiveData<MutableList<MovieDetail>>()
    val liveDataFavoritesList: LiveData<MutableList<MovieDetail>> = _liveDataFavoritesList

    var _databaseList = MutableStateFlow<HashMap<Int, Boolean>>(HashMap())
    var databaseList: StateFlow<HashMap<Int, Boolean>> = _databaseList

    init {
        val database = AppDatabaseProvider.getAppDatabase(application)
        movieDao = database.movieDao()

        createList()


    }
    fun getMovieDao(): MovieDao {
        return movieDao
    }

    private fun callMovieRepos(movies: MutableList<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieList = movies.map { movieId ->
                    movieService.getMovie(movieId, BundleKeys.API_KEY)
                }
                val currentList = _liveDataFavoritesList.value ?: emptyList()
                val updatedList: MutableList<MovieDetail> = currentList.toMutableList().apply {
                    addAll(movieList)
                }
                _liveDataFavoritesList.postValue(updatedList)
            } catch (exception: Exception) {
                _liveDataFavoritesList.postValue(arrayListOf())
            }
            liveDataLoading.postValue(false)
        }
    }


    fun setFavoritesList() {
        val favoritesList = liveDataFavoritesList.value ?: mutableListOf()
        val databaseKeys = databaseList.value.keys

        val updatedFavoritesList = favoritesList.filter { movie ->
            databaseKeys.contains(movie.id)
        }.toMutableList()

        _liveDataFavoritesList.value = updatedFavoritesList
    }


     fun createList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val hashMap = HashMap<Int, Boolean>()
                for (id in movieDao.getAllByIds()) {
                    hashMap[id] = _databaseList.value[id] == true
                }
                _databaseList.value = hashMap
                Log.d("ids", databaseList.value.toString())

            }
            setFavoritesList()
            displayGroup()


        }
    }
    fun displayGroup() {
            val nextTenKeys = setList()

            if (nextTenKeys.isNotEmpty()) {
                callMovieRepos(nextTenKeys)
            }

    }

    private fun setList(): ArrayList<Int> {

        val nextTenKeys = ArrayList<Int>(arrayListOf())
        val filteredValues = databaseList.value.filterValues { !it }.entries.take(10)
        filteredValues.forEach { entry ->
            nextTenKeys.add(entry.key)
            databaseList.value[entry.key] = true
        }



        Log.d("nextTenKeys", nextTenKeys.toString())
        return nextTenKeys
    }
}





