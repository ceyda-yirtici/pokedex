package com.example.movieproject.service

import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieCredit
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieGenre
import com.example.movieproject.model.MovieList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getMovieList(@Query("api_key") api_key:String, @Query("page") page:Int) : MovieList

    @GET("movie/{movie_id}/credits")
    suspend fun getCredit(@Path("movie_id") id: Int, @Query("api_key") api_key:String) : MovieCredit

    @GET("person/{person_id}")
    suspend fun getPerson(@Path("person_id") id: Int, @Query("api_key") api_key:String) : CastPerson

    @GET("search/movie")
    suspend fun getSearchList(@Query("api_key") api_key:String, @Query("page") page:Int, @Query("query") query: String) : MovieList

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendations(@Path("movie_id") id: Int, @Query("api_key") api_key:String, @Query("page") page:Int) : MovieList

    @GET("genre/movie/list?language=en")
    suspend fun getMovieGenres(@Query("api_key") api_key:String) : GenreList

    @GET("movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int, @Query("api_key") api_key:String) : MovieDetail

    @GET("discover/movie")
    suspend fun discover(@Query("api_key") api_key:String, @Query("page") page:Int, @Query("with_genres") with_genres:String,  @Query("vote_average.gte") min_vote: Float) : MovieList

}

