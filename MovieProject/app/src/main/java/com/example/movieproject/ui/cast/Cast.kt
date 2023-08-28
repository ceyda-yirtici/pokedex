import android.content.res.Configuration
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.movieproject.ui.theme.MovieTheme
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.movieproject.R

import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.cast.CastViewModel
import com.example.movieproject.ui.components.GridMovie
import com.example.movieproject.utils.BundleKeys
import dagger.Lazy
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Preview("default")
@Preview("light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun CastScreenPreview() {

    MovieTheme {
        val mockBackDrop = MovieDetail(
            backdrop_path = "",
            title = "String",
            original_title = "",
            poster_path = "String",
            overview = "String",
            release_date = "String",
            genre_ids = listOf(),
            genres = listOf(),
            id = 0,
            vote = 0.0,
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
        CastInfo(
            cast = mockCast,
            backdrop = mockBackDrop,
            onBackPressedDispatcher = null,
            movieList = ArrayList<MovieDetail>(arrayListOf()),
        )
    }

}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CastScreen(
    castUiState: CastViewModel.CastUiState?,
    onBackPressedDispatcher: OnBackPressedDispatcher
) {

    val cast = castUiState?.cast
    val backdropMovie = castUiState?.backDropMovie
    val movieList = castUiState?.movieList
    MovieTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MovieTheme.colors.uiBackground)
        ) {
            if (cast != null && backdropMovie != null) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        CastInfo(cast = cast, backdrop = backdropMovie, movieList = movieList,
                            onBackPressedDispatcher = onBackPressedDispatcher)
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
fun CastInfo(
    cast: CastPerson,
    backdrop: MovieDetail,
    movieList: ArrayList<MovieDetail>?,
    onBackPressedDispatcher: OnBackPressedDispatcher?,
) {
    val context = LocalContext.current

    Column {

        // Cast person photo
        val photoUrl = BundleKeys.baseImageUrl + cast.photo_path
        val backdropUrl = BundleKeys.baseImageUrlForOriginalSize + backdrop.backdrop_path
        val gradient = Brush.verticalGradient(
            colors = listOf(Color.Transparent, MovieTheme.colors.uiBackground), startY = 100.0f,
            endY = 500.0f
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (toolbar, image, backdropPhoto, name, departmentText) = createRefs()

                Image(
                    painter = rememberAsyncImagePainter(
                        model = backdropUrl,
                        error = painterResource(id = R.drawable.baseline_photo_24),
                        placeholder = painterResource(R.drawable.baseline_photo_24)
                    ),
                    contentDescription = "Cast backdrop Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(205.dp)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(gradient, blendMode = BlendMode.Multiply)
                            }
                        }
                        .constrainAs(backdropPhoto) {
                            top.linkTo(parent.top)
                        }


                )

                TopAppBar(
                    title = {
                        Text(text = "")
                    },
                    modifier = Modifier.constrainAs(toolbar) {
                        top.linkTo(parent.top)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackPressedDispatcher?.onBackPressed()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowCircleLeft,
                                modifier = Modifier.size(100.dp),
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        navigationIconContentColor = Color.White,
                        containerColor = Color.Transparent
                    )
                )
                Image(
                    painter = rememberAsyncImagePainter(
                        model = photoUrl,
                        error = painterResource(id = R.drawable.baseline_person_24),
                        placeholder = painterResource(R.drawable.baseline_person_24)
                    ),

                    contentDescription = "Cast Person Photo",
                    contentScale = ContentScale.Crop, // Center-crop the image
                    modifier = Modifier
                        .size(130.dp)
                        .border(
                            BorderStroke(4.dp, (MovieTheme.colors.uiBackground)),
                            CircleShape
                        )
                        .clip(CircleShape)
                        .background(MovieTheme.colors.uiBackground)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            bottom.linkTo(departmentText.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                Text(
                    text = cast.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MovieTheme.colors.textPrimary,
                    modifier = Modifier
                        .constrainAs(name) {
                            top.linkTo(image.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(16.dp)
                )
                if (!cast.known_for_department.isNullOrEmpty()) {
                    Text(
                        text = "${cast.known_for_department}",
                        fontSize = 14.sp,
                        color = MovieTheme.colors.textPrimary,
                        modifier = Modifier
                            .constrainAs(departmentText) {
                                top.linkTo(name.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(top = 6.dp, bottom = 16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Other cast information

        if (!cast.birthday.isNullOrEmpty()) {
            val formattedBirthday = dateConversion(cast.birthday)
            Text(
                text = "Born: $formattedBirthday",
                color = MovieTheme.colors.textPrimary,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }

        if (!cast.place_of_birth.isNullOrEmpty()) {
            Text(
                text = "From: ${cast.place_of_birth}",
                color = MovieTheme.colors.textPrimary,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Cast person biography
        cast.biography?.let {
            if (cast.biography != "")
                Text(
                    text = "Biography",
                    fontWeight = FontWeight.Bold,
                    color = MovieTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            Text(
                text = it,
                color = MovieTheme.colors.textSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
        CastMovieDisplay(movieList)


    }

}

@Composable
fun CastMovieDisplay(movieList: ArrayList<MovieDetail>?) {
    if (!movieList.isNullOrEmpty()) {
        Text(
            text = "Movies",
            color = MovieTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxHeight(),
            userScrollEnabled = true,
            verticalAlignment =  Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState(),

        ) {
            items(items = movieList, itemContent = { item ->
                GridMovie(imageUrl = item.poster_path, title = item.title) // image null kontrolü yap.
            })
        }

    }
}

@Composable
fun dateConversion(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
    val parsedDate = inputFormat.parse(date)
    return outputFormat.format(parsedDate ?: "")
}
