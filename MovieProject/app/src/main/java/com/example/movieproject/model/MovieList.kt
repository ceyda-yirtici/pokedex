package com.example.movieproject.model

import com.google.gson.annotations.SerializedName

data class  MovieList (

    @SerializedName("results") val results: ArrayList<MovieDetail>,
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val total_pages: Int,

        )