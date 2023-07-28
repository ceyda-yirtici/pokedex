package com.example.day5.service

import com.example.day5.model.Character
import com.example.day5.model.CharacterDetail
import com.example.day5.model.PokeGroup
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeService {



    @GET("api/v2/pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): CharacterDetail

}

