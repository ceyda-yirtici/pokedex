package com.example.day5.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.day5.model.Character
import com.example.day5.model.CharacterDetail
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
    private val _liveDataPokeGroup = MutableLiveData<List<CharacterDetail>>()
    val liveDataPokeGroup: LiveData<List<CharacterDetail>> = _liveDataPokeGroup

    val liveDataLoading = MutableLiveData<Boolean>()
    init {
        callPokemonRepos()
    }

    /*
    private fun callPokemonDetails(id: Int) {
        liveDataLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
             try {
                 val poke =getPokemonDetail(id)
                 _liveDataPoke.postValue(poke)

            } catch (exception: Exception) {
                ""
            }
            liveDataLoading.postValue(false)
        }
    }*/
    private fun callPokemonRepos() {
        liveDataLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {

            val pokeList = try {
                val pokemonList = mutableListOf<CharacterDetail>()
                for (i in 1..50) {
                    val pokemon = pokeService.getPokemon(i)
                    pokemonList.add(pokemon)
                }
                pokemonList
            } catch (exception: Exception) {
                emptyList()
            }
            Log.i("mvvm", pokeList.toString())
            _liveDataPokeGroup.postValue(pokeList)
            liveDataLoading.postValue(false)
        }
    }


    fun displayGroup() {
        callPokemonRepos()
    }

    fun extractIdFromUrl(url: String): Int {
        val regex = ".*/pokemon/(\\d+)/".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)?.toInt() ?: -1
    }

}