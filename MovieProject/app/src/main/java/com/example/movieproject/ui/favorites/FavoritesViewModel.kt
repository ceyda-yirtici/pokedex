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

    private val _liveDataFavoritesList = MutableLiveData<ArrayList<MovieDetail>>()
    val liveDataFavoritesList: LiveData<ArrayList<MovieDetail>> = _liveDataFavoritesList

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

    private fun callMovieRepos(movies: ArrayList<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieList = movies.map { movieId ->
                    movieService.getMovie(movieId, BundleKeys.API_KEY)
                }
                _liveDataFavoritesList.postValue(movieList as ArrayList<MovieDetail>?)
            } catch (exception: Exception) {
                _liveDataFavoritesList.postValue(arrayListOf())
            }
            liveDataLoading.postValue(false)
        }
    }

    fun refresh(){
        createList()
    }

    private fun createList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val hashMap = HashMap<Int, Boolean>()
                for (id in movieDao.getAllByIds()) {
                    Log.d("ids", id.toString())
                    hashMap[id] = false
                }
                Log.d("ids", hashMap.toString())
                _databaseList.value = hashMap
                Log.d("ids", databaseList.value.toString())

            }
            databaseList.collect { hashMap ->
                displayGroup(hashMap.toMutableMap())
            }
        }
    }
    fun displayGroup(toMutableMap: MutableMap<Int, Boolean>) {
        val nextTenKeys = setList(toMutableMap)

        if (nextTenKeys.isNotEmpty())
            callMovieRepos(nextTenKeys)
        }
    }

    private fun setList(paginationHashMap: MutableMap<Int, Boolean>): ArrayList<Int> {
        val nextTenKeys = ArrayList<Int>()
        val filteredValues = paginationHashMap.filterValues { !it }.entries.take(10)
        if (filteredValues.isNotEmpty()) {
            filteredValues.forEach { entry ->
                nextTenKeys.add(entry.key)
                paginationHashMap[entry.key] = true
            }
        }

        Log.d("nextTenKeys", nextTenKeys.toString())
        return nextTenKeys
    }



