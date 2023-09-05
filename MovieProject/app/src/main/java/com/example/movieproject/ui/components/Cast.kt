package com.example.movieproject.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.movieproject.R
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.utils.BundleKeys
import com.google.gson.annotations.SerializedName

@Composable
fun CastItem(
    person: CastPerson,
    onItemClick: (Int) -> Unit,
) {

    Box(
        modifier = Modifier
            .width(130.dp)
            .clickable { onItemClick(person.id) }
    ) {
        var photoUrl = ""
        if (person.photo_path != null) photoUrl = BundleKeys.baseImageUrl + person.photo_path

        Column {

            Image(
                painter = rememberAsyncImagePainter(
                    model = photoUrl,
                    error = painterResource(id = R.drawable.baseline_photo_220dp),
                    placeholder = painterResource(id = R.drawable.baseline_photo_220dp)
                ),
                alignment =  Alignment.Center,
                contentDescription = "Grid Cast Photo",
                contentScale = ContentScale.Crop, // Center-crop the image
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 5.dp, end = 10.dp)
                    .background(
                        color = Color.Transparent
                    )
                    .shadow(elevation = 0.dp, shape = MaterialTheme.shapes.medium, clip = true)
                    .height(120.dp)
                    .width(110.dp)


            )
            Text(
                text = person.name,
                overflow= TextOverflow.Ellipsis,
                color = MovieTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines =  1,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 5.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
            )
        }

    }

}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CastItemPreview() {
    val mockPerson = CastPerson(

        id = 0,
        name = "Gal Gadot",
        photo_path= "",
        character= "",
        biography= "",
        birthday= "",
        known_for_department= "",
        place_of_birth= "",

    )
    MovieTheme {
        CastItem(mockPerson, {})
    }
}