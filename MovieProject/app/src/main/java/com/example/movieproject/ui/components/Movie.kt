package com.example.movieproject.ui.components

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import coil.compose.rememberAsyncImagePainter
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.utils.BundleKeys
import kotlin.math.absoluteValue


@Composable
fun GridMovie(
              imageUrl: String?,
              title: String
) {

    Box(
        modifier = Modifier.width(130.dp)
    ) {
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
                    .padding(start = 10.dp, end = 10.dp)
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
                text = title,
                overflow= TextOverflow.Ellipsis,
                color = MovieTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines =  1,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
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
    genres: ArrayList<String>
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

        ConstraintLayout(
            modifier = Modifier
            .fillMaxWidth()
        )
        {
            val (image, name, desc, heart, genreList) = createRefs()
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
                        color = MovieTheme.colors.uiBackground
                    )
                    .shadow(elevation = 0.dp, shape = MaterialTheme.shapes.medium, clip = true)
                    .height(180.dp)
                    .width(120.dp)
                    .constrainAs(image) {
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                        end.linkTo(name.start)
                        start.linkTo(parent.start)
                    }

            )

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
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                        start.linkTo(image.end)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
            Text(
                text = movie.overview,
                overflow = TextOverflow.Ellipsis,
                color = MovieTheme.colors.textSecondary,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 5,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
                    .constrainAs(desc) {
                        top.linkTo(genreList.bottom)
                        start.linkTo(image.end)
                        bottom.linkTo(image.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
            val contextForToast = LocalContext.current.applicationContext
            val heartIcon: ImageVector = if (movie.heart_tag == "outline") Icons.Outlined.FavoriteBorder
            else Icons.Filled.Favorite
            val heartTint: Color = if (movie.heart_tag == "outline") MovieTheme.colors.iconInteractiveInactive
            else MovieTheme.colors.filledHeart

            IconButton(
                modifier = Modifier
                    .size(size = 50.dp)
                    .constrainAs(heart) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                onClick = {
                    Toast.makeText(contextForToast, "Click!", Toast.LENGTH_SHORT).show()
                }

            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 35.dp),
                    imageVector = heartIcon,
                    tint = heartTint,
                    contentDescription = "Heart Button"
                )
            }
            BoxWithConstraints(
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .constrainAs(genreList) {
                        top.linkTo(name.bottom)
                        bottom.linkTo(desc.top)
                        start.linkTo(image.end)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
            ) {
                val maxWidth = this@BoxWithConstraints.maxWidth
                GenreDisplay(maxWidth,genres)

            }
        }
    }
}

@Composable
private fun GenreDisplay(maxWidth: Dp, genres: ArrayList<String>){
    val visibilityList = List(genres.size) { mutableStateOf(true) }
    Row(
        modifier = Modifier.width(maxWidth),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        var totalWidth by remember { mutableStateOf(0.dp)}
        for (index in genres.indices) {
            val item = genres[index]
            var textVisibility by remember { mutableStateOf(true) }
            if(textVisibility) {
                Genre(
                    modifier = Modifier.alpha(1f),
                    elevation = 5.dp
                ) {
                    Text(
                        text = item,
                        fontSize = 10.sp,
                        softWrap = false,
                        color = MovieTheme.colors.textSecondary,
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp),
                        onTextLayout = { textLayoutResult ->
                            if (index == 0 || (visibilityList[index - 1].value)) {
                                val textWidth = textLayoutResult.size.width.dp
                                if (totalWidth + textWidth <= maxWidth) {
                                    // There's enough space, show the item
                                    totalWidth += textWidth
                                } else {
                                    // Not enough space, hide the item
                                    visibilityList[index].value = false
                                    textVisibility = false

                                }
                            } else if (!visibilityList[index - 1].value) {
                                visibilityList[index].value = false
                                textVisibility = false
                            }
                        },
                        )
                }
            }
        }
    }

}

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

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GridItemPreview() {
    MovieTheme {
        GridMovie(title = "Movie", imageUrl = "")
    }
}


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListItemPreview() {
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
    val mockGenreList = arrayListOf("Comedy", "Horror", "Drama", "Animation", "Fiction", "Western" )
    MovieTheme {
        ListMovie(mockMovie, mockGenreList)
    }
}
