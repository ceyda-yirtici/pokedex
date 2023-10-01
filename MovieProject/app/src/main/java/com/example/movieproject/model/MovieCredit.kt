package com.example.movieproject.model

import com.google.gson.annotations.SerializedName

class MovieCredit (
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: ArrayList<CastPerson>,

)