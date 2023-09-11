package com.example.movieproject.ui.favorites


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.GenreMapper
import com.example.movieproject.ui.components.ListMovie
import com.example.movieproject.ui.components.LoadingScreen
import com.example.movieproject.ui.theme.MovieTheme


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesScreenPreview() {

    MovieTheme {
        MovieInfo(
            movieUiState = null,
            navController = null,
            favoritesManager = null

        )
    }

}




@Composable
fun FavoritesScreen(
    movieUiState: FavoritesViewModel.FavoriteMoviesUiState?,
    navController: NavController,
    favoritesManager: FavoritesManager
) {
    val movieList = movieUiState?.movieList
    val loading = movieUiState?.loading
    MovieTheme {



        if (movieList != null) {

            MovieInfo(
                movieUiState= movieUiState,
                navController = navController,
                favoritesManager = favoritesManager,

                )


        } else {
            loading?.let {
                LoadingScreen(
                    isLoading = it,
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieInfo(
    movieUiState: FavoritesViewModel.FavoriteMoviesUiState?,
    navController: NavController?,
    favoritesManager: FavoritesManager?,
) {
    Column() {
        Box {
            TopAppBar(
                title = {
                    Text(text = "Favorite Movies",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MovieTheme.colors.textPrimary)
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MovieTheme.colors.uiBackground
                ),
                modifier = Modifier
                    .zIndex(4f)
                    .background(MovieTheme.colors.uiBorder)
                    .fillMaxWidth()
                    .align(Alignment.Center),
            )


        }
        movieUiState?.movieList?.let { FavListDisplay(
            movieList = it,
            favoritesManager,
            movieUiState,)
        }



    }


}


@Composable
fun FavListDisplay(
    movieList: ArrayList<MovieDetail>,
    favoritesManager: FavoritesManager?,
    movieUiState: FavoritesViewModel.FavoriteMoviesUiState,
) {
    if (movieList.size != 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MovieTheme.colors.uiBackground),
                userScrollEnabled = true,
                state = rememberLazyListState(),

                ) {
                items(movieList) { item ->

                    var clicked by remember { mutableStateOf(-1) }
                    item.let {
                        val genres = item.genres?.map { it.genre_name }
                        ListMovie(item, genres as ArrayList<String>, favoritesManager,
                            onHeartButtonClick = { clicked = item.id })
                    }

                    if (clicked != -1) {
                        movieUiState.movieList.remove(item)
                    }

                }
            }

    }
}



