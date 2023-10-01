package com.example.movieproject.model

import com.google.gson.annotations.SerializedName

class PersonCredit (
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val castMovies: ArrayList<MovieDetail>,

    )