package com.example.movieproject.ui.moviedetail

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.movieproject.R
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieGenre
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.components.CastItem
import com.example.movieproject.ui.components.Date
import com.example.movieproject.ui.components.Genre
import com.example.movieproject.ui.components.GridMovie
import com.example.movieproject.ui.components.LoadingScreen
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.utils.BundleKeys
import java.util.Locale


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CastScreenPreview() {

    MovieTheme {
        val mockGenre = MovieGenre(1, "Action")
        val mockMovie = MovieDetail(
            backdrop_path = "",
            title = "Indiana Jones and the Dial of Destiny",
            original_title = "",
            poster_path = "String",
            overview = "Teen Miles Morales becomes the Spider-Man of" +
                    " his universe and must join with five spider-powered" +
                    " individuals from other dimensions to stop a threat for all realities.",
            release_date = "2023-9-5",
            genre_ids = listOf(),
            genres = List(7) { mockGenre },
            id = 0,
            vote = 7.656,
            popularity = 0.0,
            heart_tag = "String"
        )
        val mockCast = CastPerson(
            id = 1,
            name = "John Doe",
            photo_path = "/AbXKtWQwuDiwhoQLh34VRglwuBE.jpg",
            character = "Character Name",
            biography = "Lorem ipsum dolor sit amet...",
            birthday = "1990-01-01",
            known_for_department = "Acting",
            place_of_birth = "City, Country"
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MovieTheme.colors.uiBackground)
        ) {
            item {
                MovieInfo(
                    movie = mockMovie,
                    movieList = mockMovie as LazyPagingItems<MovieDetail>,
                    onBackPressedDispatcher = null,
                    castList = ArrayList(arrayListOf(mockCast)),
                    navController = rememberNavController(),
                    favoritesManager = null,

                )
            }
        }
    }

}




@Composable
fun MovieScreen(
    movieUiState: DetailsViewModel.MovieUiState?,
    onBackPressedDispatcher: OnBackPressedDispatcher,
    navController: NavController,
    favoritesManager: FavoritesManager
) {

    val movie = movieUiState?.movie
    val movieList = movieUiState?.movieList?.collectAsLazyPagingItems()
    val castList = movieUiState?.castList
    val loading = movieUiState?.loading
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
                        MovieInfo(
                            movie = movie,
                            movieList = movieList,
                            onBackPressedDispatcher = onBackPressedDispatcher,
                            castList = castList,
                            navController,
                            favoritesManager
                        )
                    }
                }
            } else {
                loading?.let {
                    LoadingScreen(
                        isLoading = it,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun MovieInfo(
    movie: MovieDetail,
    movieList: LazyPagingItems<MovieDetail>?,
    onBackPressedDispatcher: OnBackPressedDispatcher?,
    castList: ArrayList<CastPerson>?,
    navController: NavController,
    favoritesManager: FavoritesManager?,
) {

    Column() {

        Box {
            TopAppBar(
                title = {
                    Text(text = "")
                },
                modifier = Modifier.zIndex(4f),
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        val backIcon = Icons.Filled.ArrowBack
                        val buttonColor = MovieTheme.colors.iconInteractive
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(buttonColor)
                        ) {

                            Icon(
                                imageVector = backIcon,
                                tint = MovieTheme.colors.textSecondary,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(8.dp),
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    navigationIconContentColor = MovieTheme.colors.iconPrimary,
                    containerColor = Color.Transparent
                )
            )

            val gradient = Brush.verticalGradient(
                colors = MovieTheme.colors.gradientBackDrop,
            )
            val photoUrl = BundleKeys.baseImageUrlForOriginalSize + movie.backdrop_path
            Image(
                painter = rememberAsyncImagePainter(
                    model =photoUrl ,
                    error = painterResource(id = R.drawable.baseline_photo_24),
                    placeholder = painterResource(R.drawable.baseline_photo_24)
                ),
                contentDescription = "Cast backdrop Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .requiredHeight(205.dp)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.SrcOver)
                        }
                    }

            )

        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = movie.title,
                fontWeight = FontWeight.Bold,
                color = MovieTheme.colors.textPrimary,
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 70.dp, top = 16.dp, bottom = 16.dp,)
                    .align(Alignment.CenterStart)
            )
            var heartTag by remember { mutableStateOf(movie.heart_tag) }

            IconButton(
                modifier = Modifier
                    .size(size = 70.dp)
                    .align(Alignment.TopEnd)
                    .padding(15.dp),
                onClick = {
                    if (movie.heart_tag == "filled"){
                        favoritesManager?.removeMovieFromDB(movie, null)
                        movie.heart_tag = "outline"
                        heartTag = "outline"
                    }
                    else{
                        favoritesManager?.addMovieToDB(movie, null)
                        movie.heart_tag = "filled"
                        heartTag = "filled"
                    }
                }

            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 60.dp)
                        .align(Alignment.TopEnd),
                    imageVector = if (heartTag == "filled")Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    tint = if (heartTag== "filled") MovieTheme.colors.filledHeart
                    else MovieTheme.colors.iconInteractiveInactive,
                    contentDescription = "Heart Button"
                )
            }


        }
        //Spacer(modifier = Modifier.height(16.dp))

        Column () {
            if (movie.genres != null) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),) {
                            Date(modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.CenterVertically)) {
                                Text(
                                    text = movie.release_date.subSequence(0, 4).toString(),
                                    modifier = Modifier.padding(
                                        start = 5.dp,
                                        end = 5.dp,
                                        top = 1.dp,
                                        bottom = 2.dp
                                    ),
                                    fontSize = 12.sp,
                                    color = MovieTheme.colors.textInteractive
                                )
                            }
                            Icon(
                                modifier = Modifier
                                    .padding(bottom = 3.dp)
                                    .fillMaxHeight(),
                                imageVector = Icons.Outlined.StarOutline,
                                tint = Color.Yellow,
                                contentDescription = "Vote Start"


                            )
                            Text(
                                text = String.format(Locale.US, "%.1f", movie.vote),
                                modifier = Modifier
                                    .padding(
                                        end = 5.dp,
                                        top = 1.dp,
                                        bottom = 2.dp
                                    )
                                    .align(Alignment.CenterVertically),
                                fontSize = 12.sp,
                                color = MovieTheme.colors.textSecondary
                            )
                        }

                    }
                    for (genre in movie.genres) {
                        Genre( modifier = Modifier.wrapContentWidth()) {
                           Text(text = genre.genre_name,
                               modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 1.dp, bottom = 2.dp),
                               fontSize = 12.sp,
                                color = MovieTheme.colors.textInteractive)
                        }

                    }
                }
            }

            Text(
                text = movie.overview,
                fontWeight = FontWeight.Light,
                color = MovieTheme.colors.textSecondary,
                fontSize = 19.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
            CastListDisplay (castList = castList, navController)
            Spacer(modifier = Modifier.height(16.dp))
            MovieListDisplay(movieList = movieList, navController)



    }


}

@Composable
fun CastListDisplay(castList: ArrayList<CastPerson>?, navController: NavController) {

    var clicked by remember { mutableStateOf(-1) }
    val bundle by remember { mutableStateOf(Bundle())}
    if (!castList.isNullOrEmpty()) {
        Text(
            text = "Cast",
            color = MovieTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxHeight(),
            userScrollEnabled = true,
            verticalAlignment =  Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState(),

            ) {
            items(items = castList, itemContent = { item ->

                CastItem(item, onItemClick = {clicked = item.id})


            })

        }
        if(clicked != -1){
            bundle.apply {
                putInt(BundleKeys.REQUEST_PERSON_ID, clicked)
                navController.navigate(R.id.action_cast, bundle)

            }
        }

    }
}



@Composable
fun MovieListDisplay(movieList: LazyPagingItems<MovieDetail>?, navController: NavController) {
    val bundle by remember { mutableStateOf(Bundle())}
    var clicked by remember { mutableStateOf(-1) }
    if (movieList != null && movieList.itemCount != 0) {
        Text(
            text = "Recommendations",
            color = MovieTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxHeight(),
            userScrollEnabled = true,
            verticalAlignment =  Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState(),

            ) {
            items(items = movieList.itemSnapshotList, itemContent = { item ->

                item?.let {
                    GridMovie(item,  onItemClick = {clicked = item.id})
                }
                if(clicked != -1){
                    bundle.apply {
                        putInt(BundleKeys.REQUEST_MOVIE_ID, clicked)
                        navController.navigate(R.id.action_detail, bundle)

                    }
                }

            })
        }
    }
}
