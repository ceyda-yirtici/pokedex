package com.example.day5.model

import com.google.gson.annotations.SerializedName

data class PokeGroup(

    @SerializedName("results") val characters: ArrayList<Character?>,
)

