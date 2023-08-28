package com.example.movieproject.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.movieproject.R
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.ui.theme.Shapes
import com.example.movieproject.utils.BundleKeys


@Composable
fun GridMovie(
              imageUrl: String?,
              title: String
) {

    Box(
        modifier = Modifier
    ) {
        var photoUrl = ""
        if (imageUrl != null) photoUrl = BundleKeys.baseImageUrl + imageUrl

        ConstraintLayout {
            val (image, name) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(
                    model = photoUrl,
                    error = painterResource(id = R.drawable.baseline_photo_24),
                    placeholder = painterResource(id = R.drawable.baseline_photo_24)
                ),
                alignment =  Alignment.Center,
                contentDescription = "Cast Movie Photo",
                contentScale = ContentScale.Crop, // Center-crop the image
                modifier = Modifier.padding(start = 10.dp, end = 10.dp).background(
                    color = MovieTheme.colors.uiBackground)
                    .height(165.dp)
                    .width(110.dp).constrainAs(image) {
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
                modifier = Modifier.padding(3.dp).constrainAs(name) {
                    top.linkTo(image.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
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
@Preview("large font", fontScale = 2f)
@Composable
private fun CardPreview() {
    MovieTheme {
        GridMovie(title = "Movie", imageUrl = "")
    }
}
