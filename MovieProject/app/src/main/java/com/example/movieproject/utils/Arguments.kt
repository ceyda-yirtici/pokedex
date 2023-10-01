package com.example.movieproject.utils

import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry

data class FavoritesFragmentArgs(
    val navigateToRoute: (String) -> Unit,
    val modifier: Modifier = Modifier,
    val onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    val from: NavBackStackEntry
    // Add other arguments as needed
) {
}