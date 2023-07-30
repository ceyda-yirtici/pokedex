package com.example.day5.model

import com.google.gson.annotations.SerializedName

data class AbilityDetails (
    @SerializedName("ability") val ability: CharacterAbility)