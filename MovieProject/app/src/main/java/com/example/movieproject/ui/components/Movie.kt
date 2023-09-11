package com.example.movieproject.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.utils.BundleKeys



@Composable
fun GridMovie(
              movie:MovieDetail
) {

    Box(
        modifier = Modifier.width(130.dp)
    ) {
        val imageUrl = movie.poster_path
        var photoUrl = ""
        if (imageUrl != null) photoUrl = BundleKeys.baseImageUrl + imageUrl

        ConstraintLayout {
            val (image, name) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(
                    model = photoUrl,
                    error = painterResource(id = R.drawable.baseline_photo_220dp),
                    placeholder = painterResource(id = R.drawable.baseline_photo_220dp)
                ),
                alignment =  Alignment.Center,
                contentDescription = "Grid Movie Photo",
                contentScale = ContentScale.Fit, // Center-crop the image
                modifier = Modifier
                    .padding(start = 5.dp, end = 10.dp)
                    .background(
                        color = Color.Transparent
                    )
                    .shadow(elevation = 0.dp, shape = MaterialTheme.shapes.medium, clip = true)
                    .height(165.dp)
                    .width(110.dp)
                    .constrainAs(image) {
                        bottom.linkTo(name.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }

            )
            Text(
                text = movie.title,
                overflow= TextOverflow.Ellipsis,
                color = MovieTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines =  1,
                modifier = Modifier
                    .padding(start = 5.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
                    .constrainAs(name) {
                        top.linkTo(image.bottom)
                        start.linkTo(image.start)
                        end.linkTo(image.end)
                    }
            )
        }

    }

}

@Composable
fun ListMovie(
    movie: MovieDetail,
    genres: ArrayList<String>,
    favoritesManager: FavoritesManager?,
    onHeartButtonClick:  (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .fillMaxWidth()
            .height(192.dp)
            .background(MovieTheme.colors.uiBackground)
    ) {
        var photoUrl = ""
        if (movie.poster_path != null) photoUrl = BundleKeys.baseImageUrl + movie.poster_path

        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement =  Arrangement.spacedBy(0.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = photoUrl,
                    error = painterResource(id = R.drawable.baseline_photo_220dp),
                    placeholder = painterResource(id = R.drawable.baseline_photo_220dp)
                ),
                alignment = Alignment.CenterStart,
                contentDescription = "List Movie Photo",
                contentScale = ContentScale.Fit, // Center-crop the image
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
                    .background(
                        color = Color.Transparent
                    )
                    .shadow(elevation = 0.dp, shape = MaterialTheme.shapes.medium, clip = true)
                    .height(180.dp)
                    .width(120.dp)

            )
            Box( modifier = Modifier.wrapContentWidth()){
                Column() {
                    Box (){
                        var heartTag by remember { mutableStateOf(movie.heart_tag) }
                        Text(
                            text = movie.title,
                            overflow = TextOverflow.Ellipsis,
                            color = MovieTheme.colors.textPrimary,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 2,
                            modifier = Modifier
                                .padding(start = 10.dp, end = 50.dp, top = 5.dp, bottom = 3.dp)
                                .align(Alignment.CenterStart)
                                .fillMaxWidth()
                        )
                        IconButton(
                            modifier = Modifier
                                .size(size = 50.dp)
                                .align(Alignment.TopEnd),
                            onClick = {
                                onHeartButtonClick(movie.id)
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
                                    .size(size = 35.dp),
                                imageVector = if (heartTag == "filled")Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                tint = if (heartTag== "filled") MovieTheme.colors.filledHeart
                                else MovieTheme.colors.iconInteractiveInactive,
                                contentDescription = "Heart Button"
                            )
                        }
                    }
                    Date(modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                        .wrapContentWidth()
                    ) {

                        Text(
                            text = movie.release_date.subSequence(0,4).toString(),
                            fontSize = 10.sp,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            color = MovieTheme.colors.textSecondary,
                            modifier = Modifier
                                .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp),
                        )
                    }


                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
                            .wrapContentWidth()
                    ) {
                        GenreDisplay(genres)

                    }
                    Text(
                        text = movie.overview,
                        overflow = TextOverflow.Ellipsis,
                        color = MovieTheme.colors.textSecondary,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Light,
                        fontSize = 13.sp,
                        maxLines = 5,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)

                    )


                }

            }
            
        }

    }
}

@Composable
private fun GenreDisplay(genres: ArrayList<String>){
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .zIndex(5f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        var textVisibility by remember { mutableStateOf(true) }
        for (index in genres.indices) {
            val item = genres[index]
            if(textVisibility) {
                Genre(
                    modifier = Modifier
                        .alpha(1f)
                        .wrapContentWidth(),
                    elevation = 5.dp
                ) {
                    Text(
                        text = item,
                        fontSize = 10.sp,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        color = MovieTheme.colors.textSecondary,
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp),
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.isLineEllipsized(0))
                            {
                                textVisibility = false
                            }
                        },
                        )
                }
            }
        }
    }

}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GridItemPreview() {
    val mockMovie = MovieDetail(
        backdrop_path = "",
        title = "Spider-Man: Across the Spider Verse",
        original_title = "",
        poster_path = "String",
        overview = "Lorem ipsum dolor sit amet...",
        release_date = "String",
        genre_ids = listOf(),
        genres = listOf(),
        id = 0,
        vote = 0.0,
        popularity = 0.0,
        heart_tag = "String"
    )
    MovieTheme {
        GridMovie(mockMovie)
    }
}


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListItemPreview() {
    val mockMovie = MovieDetail(
        backdrop_path = "",
        title = "Barbie",
        original_title = "",
        poster_path = "String",
        overview = "Lorem ipsum dolor sit amet...",
        release_date = "String",
        genre_ids = listOf(),
        genres = listOf(),
        id = 0,
        vote = 0.0,
        popularity = 0.0,
        heart_tag = "String"
    )
    val mockGenreList = arrayListOf("Comedy", "Horror", "Drama", "Animation", "Fiction", "Western" )
    MovieTheme {
        ListMovie(
            mockMovie,
            mockGenreList,
            null, {})
    }
}
