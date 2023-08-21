package com.example.movieproject.ui.components

import androidx.compose.runtime.Immutable

@Immutable
data class OrderLine(
    val movie: Movie,
)
@Immutable
data class Movie(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val tagline: String = "",
    val tags: Set<String> = emptySet()
)