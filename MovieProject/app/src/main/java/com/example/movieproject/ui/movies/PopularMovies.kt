package com.example.movieproject.ui.movies

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.paging.compose.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.GenreMapper
import com.example.movieproject.ui.components.GridMovie
import com.example.movieproject.ui.components.ListMovie
import com.example.movieproject.ui.components.LoadingScreen
import com.example.movieproject.ui.theme.MovieTheme
import kotlinx.coroutines.flow.Flow


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PopularScreenPreview() {

    MovieTheme {
        MovieInfo(
            movieUiState = null,
            navController = null,
            favoritesManager = null

        )
    }

}




@Composable
fun PopularMoviesScreen(
    movieUiState: MoviesViewModel.PopularMoviesUiState?,
    navController: NavController,
    favoritesManager: FavoritesManager
) {
    val movieList = movieUiState?.movieList?.collectAsLazyPagingItems()
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
    movieUiState: MoviesViewModel.PopularMoviesUiState?,
    navController: NavController?,
    favoritesManager: FavoritesManager?,
) {
    var viewType  by remember { mutableStateOf( movieUiState?.viewType)}

    Column() {
        Box(modifier = Modifier) {
            TopAppBar(
                title = {
                    Text(text = "Popular Movies",
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
            IconButton(
                modifier = Modifier
                    .zIndex(4f)
                    .background(Color.Transparent)
                    .align(Alignment.CenterStart),
                onClick = {
                if (viewType == 1) viewType = 2
                else if (viewType == 2) viewType = 1

            }) {
                var viewIcon: ImageVector = Icons.Filled.ViewList
                if (viewType == 1)
                    viewIcon = Icons.Filled.GridView
                else if (viewType == 2)
                    viewIcon = Icons.Filled.ViewList
                Icon(
                    imageVector = viewIcon,
                    tint = MovieTheme.colors.textSecondary,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                    contentDescription = "View Type",
                )
            }
        }
        movieUiState?.movieList?.let { MovieListDisplay(movieList = it,
            favoritesManager,
             movieUiState,
             viewType)
             }



    }


}


@Composable
fun MovieListDisplay(
    movieList: Flow<PagingData<MovieDetail>>,
    favoritesManager: FavoritesManager?,
    movieUiState: MoviesViewModel.PopularMoviesUiState,
    viewType: Int?,
) {
    val popularMovies = movieList.collectAsLazyPagingItems()
    if (popularMovies.itemCount != 0) {
        if (viewType == 1)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MovieTheme.colors.uiBackground),
                userScrollEnabled = true,
                verticalArrangement = Arrangement.Bottom,
                state = rememberLazyListState(),

                ) {
                items(popularMovies) { item ->

                    var clicked by remember { mutableStateOf(-1) }
                    item?.let {
                        ListMovie(item, GenreMapper.map(item), favoritesManager,
                            onHeartButtonClick = { clicked = item.id })
                    }

                    if (clicked != -1) {
                        if (movieUiState.favoritesList.contains(clicked))
                            movieUiState.favoritesList.remove(clicked)
                        else movieUiState.favoritesList.add(clicked)
                    }

                }
            }
        else if (viewType == 2) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),

                ) {
                items(popularMovies.itemCount)
                { index ->
                    popularMovies[index]?.let { item ->
                        GridMovie(item)
                    }
                }
            }
        }
    }
}



