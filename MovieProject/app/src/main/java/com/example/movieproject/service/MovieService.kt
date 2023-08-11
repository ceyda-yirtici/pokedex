package com.example.movieproject.service

import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieGenre
import com.example.movieproject.model.MovieList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getMovieList(@Query("api_key") api_key:String, @Query("page") page:Int) : MovieList


    @GET("search/movie")
    suspend fun getSearchList(@Query("api_key") api_key:String, @Query("page") page:Int, @Query("query") query: String) : MovieList

    @GET("genre/movie/list?language=en")
    suspend fun getMovieGenres(@Query("api_key") api_key:String) : GenreList

    @GET("movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int, @Query("api_key") api_key:String) : MovieDetail

    @GET("discover/movie")
    suspend fun discover(@Query("api_key") api_key:String, @Query("page") page:Int, @Query("with_genres") with_genres:String) : MovieList

}

