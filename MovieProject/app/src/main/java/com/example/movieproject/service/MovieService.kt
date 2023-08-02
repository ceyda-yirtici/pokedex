package com.example.movieproject.service

import com.example.movieproject.model.MovieList
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getMovieList(@Query("api_key") api_key:String, @Query("page") page:Int) : MovieList

}

