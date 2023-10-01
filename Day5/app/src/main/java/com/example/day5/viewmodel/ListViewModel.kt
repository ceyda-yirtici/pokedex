package com.example.day5.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.day5.model.CharacterDetail
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

    private val _liveDataPokeGroup = MutableLiveData<List<CharacterDetail>>()
    val liveDataPokeGroup: LiveData<List<CharacterDetail>> = _liveDataPokeGroup

    val liveDataLoading = MutableLiveData<Boolean>()

    private val _pokeCount = MutableLiveData(1)
    val pokeCount: LiveData<Int> = _pokeCount
    // LiveData for buttons enability
    private val _isForwardButtonEnabled = MutableLiveData(true)
    val isForwardButtonEnabled: LiveData<Boolean> = _isForwardButtonEnabled

    private val _isBackwardButtonEnabled = MutableLiveData(false)
    val isBackwardButtonEnabled: LiveData<Boolean> = _isBackwardButtonEnabled

    fun setPokeCount(count: Int) {
        viewModelScope.launch {
            _pokeCount.value = count

            // Update the enability of the buttons based on the new pokeCount value
            _isForwardButtonEnabled.value = count <= 60
            _isBackwardButtonEnabled.value = count >= 2
        }
    }
    init {
        callPokemonRepos(1)
    }




    private fun callPokemonRepos(pokeCount: Int) {
        liveDataLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {

            val pokeList = try {
                val pokemonList = mutableListOf<CharacterDetail>()
                for (i in pokeCount..pokeCount+19) {
                    val pokemon = pokeService.getPokemon(i)
                    pokemonList.add(pokemon)
                }
                pokemonList
            } catch (exception: Exception) {
                emptyList()
            }

            _liveDataPokeGroup.postValue(pokeList)
            liveDataLoading.postValue(false)
        }
    }


    fun displayGroup(pokeCount: Int) {
        callPokemonRepos(pokeCount)
    }

    fun extractIdFromUrl(url: String): Int {
        val regex = ".*/pokemon/(\\d+)/".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)?.toInt() ?: -1
    }

}