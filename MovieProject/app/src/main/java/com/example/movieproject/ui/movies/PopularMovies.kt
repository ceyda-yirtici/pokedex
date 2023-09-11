package com.example.movieproject.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.GenreMapper
import com.example.movieproject.ui.components.GridMovie
import com.example.movieproject.ui.components.ListMovie
import com.example.movieproject.ui.components.LoadingScreen
import com.example.movieproject.ui.components.SearchBarUI
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.utils.BundleKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PopularScreenPreview() {

    MovieTheme {
        MovieInfo(
            movieUiState = null,
            navController = null,
            favoritesManager = null,

            )
    }

}




@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun PopularMoviesScreen(
    movieUiState: MoviesViewModel.PopularMoviesUiState?,
    navController: NavController,
    favoritesManager: FavoritesManager
) {
    val movieList = movieUiState?.movieList?.collectAsLazyPagingItems()
    val loading =  movieUiState?.loading
    var searching by remember { mutableStateOf(movieUiState?.searching) }
    MovieTheme {

        if (searching == true){
            UserSearchUI(movieUiState,
                navController,
                favoritesManager,
                onSearching = { searching = it
                })
        }
        else {
            if (movieList != null) {
            MovieInfo(
                movieUiState= movieUiState,
                navController = navController,
                favoritesManager = favoritesManager,
                onSearching = { searching = it
                }
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
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun UserSearchUI(
    movieUiState: MoviesViewModel.PopularMoviesUiState?,
    navController: NavController,
    favoritesManager: FavoritesManager,
    onSearching : (Boolean) -> Unit = {},

    ) {
    var searchTextData by remember { mutableStateOf(movieUiState?.searchText)}
    var searchedValues by remember { mutableStateOf(movieUiState?.movieList)}

    if (movieUiState != null && searchedValues != null) {
        SearchBarUI(
            searchText = searchTextData ?: "",
            placeholderText = "Search users",
            onSearchTextChanged = { searchTextData = it
                searchedValues = movieUiState?.movieList
            },
            onClearClick = {  searchTextData = ""
                searchedValues= flowOf()
                onSearching(false)
            },
            matchesFound = movieUiState.movieList.collectAsLazyPagingItems().itemCount > 0,
            results = {
                MovieListDisplay(
                    movieList = searchedValues,
                    favoritesManager = favoritesManager,
                    movieUiState= movieUiState,
                    viewType = 1,
                    navController = navController,
                )
            }
        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieInfo(
    movieUiState: MoviesViewModel.PopularMoviesUiState?,
    navController: NavController?,
    favoritesManager: FavoritesManager?,
    onSearching : (Boolean) -> Unit = {},
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
            IconButton(
                modifier = Modifier
                    .zIndex(4f)
                    .background(Color.Transparent)
                    .align(Alignment.CenterEnd),
                onClick = {
                    onSearching(true)
                }) {
                Icon(
                    imageVector = Icons.Filled.Search,
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
            viewType,
            navController)
        }



    }


}


@Composable
fun MovieListDisplay(
    movieList: Flow<PagingData<MovieDetail>>?,
    favoritesManager: FavoritesManager?,
    movieUiState: MoviesViewModel.PopularMoviesUiState,
    viewType: Int?,
    navController: NavController?,
) {
    val movies = movieList?.collectAsLazyPagingItems()
    val bundle by remember { mutableStateOf(Bundle())}
    var itemClicked by remember { mutableStateOf(-1) }
    if (movies != null) {
        if (viewType == 1)
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
                        ListMovie(item, GenreMapper.map(item), favoritesManager,
                            onHeartButtonClick = { heartClicked = item.id }, onItemClick = {itemClicked = item.id})
                    }

                    if (heartClicked != -1) {
                        if (movieUiState.favoritesList.contains(heartClicked))
                            movieUiState.favoritesList.remove(heartClicked)
                        else movieUiState.favoritesList.add(heartClicked)
                    }


                }
            }
        else if (viewType == 2) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),

                ) {
                items(movies.itemCount)
                { index ->
                    movies[index]?.let { item ->
                        GridMovie(item,  onItemClick = {itemClicked = item.id})
                    }
                }
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



