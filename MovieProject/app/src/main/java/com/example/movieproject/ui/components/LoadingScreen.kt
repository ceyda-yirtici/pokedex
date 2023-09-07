package com.example.movieproject.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.example.movieproject.ui.theme.MovieTheme

@Composable
fun LoadingScreen(isLoading: Boolean, ) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier, color= MovieTheme.colors.brandSecondary,
                trackColor = MovieTheme.colors.textPrimary)

        }
    }
}

@Composable
@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun LoadingScreenPreview() {
    MovieTheme {
        LoadingScreen(isLoading = true)
    }

}
