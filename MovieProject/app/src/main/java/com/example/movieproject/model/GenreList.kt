package com.example.movieproject.model

import com.google.gson.annotations.SerializedName

data class GenreList (
    @SerializedName("genres") val genres: List<MovieGenre>)