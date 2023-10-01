package com.example.movieproject.model

import com.google.gson.annotations.SerializedName

data class MovieGenre (
   @SerializedName("id") val id:Int,
   @SerializedName("name") val genre_name: String,

)