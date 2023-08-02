package com.example.movieproject.ui.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieproject.service.MovieService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val movieService: MovieService
)
    : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is watchlist Fragment"
    }
    val text: LiveData<String> = _text
}