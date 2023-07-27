package com.example.day5.service

import com.example.day5.model.PokeGroup
import retrofit2.http.GET

interface PokeService {

    @GET("api/v2/pokemon")
    suspend fun getPokemonRepos(): PokeGroup
}

