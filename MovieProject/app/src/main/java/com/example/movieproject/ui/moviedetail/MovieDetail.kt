package com.example.movieproject.ui.moviedetail

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.cast.CastViewModel
import com.example.movieproject.ui.theme.MovieTheme

@Composable
fun MovieScreen(
    movieUiState: DetailsViewModel.MovieUiState?,
    onBackPressedDispatcher: OnBackPressedDispatcher
) {

    val movie = movieUiState?.movie
    val movieList = movieUiState?.movieList
    val castList = movieUiState?.castList
    MovieTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MovieTheme.colors.uiBackground)
        ) {
            if (movie != null) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        MovieInfo(movie = movie, movieList = movieList,
                            onBackPressedDispatcher = onBackPressedDispatcher, castList = castList)
                    }

                }
            } else {
                // Handle loading or error state here
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieInfo(movie: MovieDetail,
              movieList: ArrayList<MovieDetail>?,
              onBackPressedDispatcher: OnBackPressedDispatcher,
              castList: ArrayList<CastPerson>?) {


    Column(){

        TopAppBar(
            title = {
                Text(text = "")
            },
            modifier = Modifier,
            navigationIcon = {
                IconButton(onClick = {
                    onBackPressedDispatcher.onBackPressed()
                }) {
                    val backIcon = Icons.Filled.ArrowBack
                    val buttonColor = MovieTheme.colors.iconInteractive
                    Box(
                        modifier = Modifier.size(80.dp).background(buttonColor)
                    ) {

                        Icon(
                            imageVector = backIcon,
                            tint = MovieTheme.colors.textSecondary,
                            modifier = Modifier.size(80.dp).padding(8.dp),
                            contentDescription = "Back",
                        )
                    }
                }
            }
        )

        

    }


}
