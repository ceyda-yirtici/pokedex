package com.example.day5.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.day5.model.PokeGroup
import com.example.day5.service.PokeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val pokeService: PokeService
) : ViewModel() {

    private val _liveDataPokeGroup = MutableLiveData<PokeGroup>()
    val liveDataPokeGroup: LiveData<PokeGroup> = _liveDataPokeGroup

    val liveDataLoading = MutableLiveData<Boolean>()

    init {
        callPokemonRepos()
    }

    private fun callPokemonRepos() {
        liveDataLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val projectList = try {
                pokeService.getPokemonRepos()
            } catch (exception: Exception) {
                ""
            }
            _liveDataPokeGroup.postValue(projectList as PokeGroup?)
            liveDataLoading.postValue(false)
        }
    }

    fun display() {
        callPokemonRepos()
    }

}