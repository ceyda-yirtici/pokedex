package com.example.movieproject.ui.discovered

import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.paging.compose.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.GenreMapper
import com.example.movieproject.ui.components.ListMovie
import com.example.movieproject.ui.components.LoadingScreen
import com.example.movieproject.ui.discover.DiscoveredViewModel
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.utils.BundleKeys
import kotlinx.coroutines.flow.Flow


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiscoveredScreenPreview() {

    MovieTheme {
        MovieInfo(
            movieUiState = null,
            navController = null,
            favoritesManager = null,
            )
    }

}




@Composable
fun DiscoveredScreen(
    movieUiState: DiscoveredViewModel.DiscoveredMoviesUiState?,
    navController: NavController,
    favoritesManager: FavoritesManager
) {
    val movieList = movieUiState?.movieList?.collectAsLazyPagingItems()
    val loading =  movieUiState?.loading
    MovieTheme {
            if (movieList != null) {
                MovieInfo(
                    movieUiState= movieUiState,
                    navController = navController,
                    favoritesManager = favoritesManager,

                )
            }else {
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
    movieUiState: DiscoveredViewModel.DiscoveredMoviesUiState?,
    navController: NavController?,
    favoritesManager: FavoritesManager?,
) {
    Column() {
        Box(modifier = Modifier) {
            TopAppBar(
                title = {
                    Text(text = "Discovered Movies",
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
        movieUiState?.movieList?.let { MovieListDisplay(movieList = it,
            favoritesManager,
            movieUiState,
            navController)
        }



    }


}


@Composable
fun MovieListDisplay(
    movieList: Flow<PagingData<MovieDetail>>?,
    favoritesManager: FavoritesManager?,
    movieUiState: DiscoveredViewModel.DiscoveredMoviesUiState,
    navController: NavController?,
) {
    val movies = movieList?.collectAsLazyPagingItems()
    val bundle by remember { mutableStateOf(Bundle())}
    var itemClicked by remember { mutableStateOf(-1) }
    if (movies != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MovieTheme.colors.uiBackground),
            userScrollEnabled = true,
            verticalArrangement = Arrangement.Bottom,
            state = rememberLazyListState(),

            ) {

            items(movies) { item ->

                var heartClicked by remember { mutableStateOf(-1) }
                item?.let {
                    ListMovie(item,
                        GenreMapper.map(item),
                        favoritesManager,
                        onHeartButtonClick = { heartClicked = item.id },
                        onItemClick = { itemClicked = item.id })
                }

                if (heartClicked != -1) {
                    if (movieUiState.favoritesList.contains(heartClicked))
                        movieUiState.favoritesList.remove(heartClicked)
                    else movieUiState.favoritesList.add(heartClicked)
                }


            }


            if (itemClicked != -1) {
                bundle.apply {
                    putInt(BundleKeys.REQUEST_MOVIE_ID, itemClicked)
                    navController?.navigate(R.id.action_detail, bundle)

                }
            }
        }

    }
}



