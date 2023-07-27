package com.example.day5.model

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val avatarUrl: String?,
)
