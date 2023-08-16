package com.example.movieproject.model

import com.google.gson.annotations.SerializedName

data class CastPerson (
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("gender") val gender: Int,
    @SerializedName("order") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("popularity") val popularity : Double,
    @SerializedName("profile_path") val photo_path: String?,
    @SerializedName("character") val character: String,
    @SerializedName("biography") val biography: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("known_for_department") val known_for_department: String,
)