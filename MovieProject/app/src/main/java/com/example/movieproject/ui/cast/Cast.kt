import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter

import com.example.movieproject.model.CastPerson
import com.example.movieproject.utils.BundleKeys
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SuspiciousIndentation")
@Preview("Cast Screen - Light Theme")
@Composable
fun CastScreenPreview() {
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
        CastScreen(cast = mockCast)

}

@Composable
fun CastScreen(cast : CastPerson?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (cast != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    CastInfo(cast = cast)
                }
            }
        } else {
            // Handle loading or error state here
        }
    }
}

@Composable
fun CastInfo(cast: CastPerson) {
    val context = LocalContext.current

    Column {
        // Cast person photo
        val photoUrl = BundleKeys.baseImageUrl + cast.photo_path
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (image, name, departmentText) = createRefs()

                Image(
                    painter = rememberAsyncImagePainter(
                        model = photoUrl
                    ),
                    contentDescription = "Cast Person Photo",
                    contentScale = ContentScale.Crop, // Center-crop the image
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(color = Color.Gray)
                        .constrainAs(image) {
                            centerTo(parent)
                        }
                )
                Text(
                    text = cast.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .constrainAs(name) {
                            top.linkTo(image.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                if (!cast.known_for_department.isNullOrEmpty()) {
                    Text(
                        text = "${cast.known_for_department}",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .constrainAs(departmentText) {
                                top.linkTo(name.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(30.dp))


        if (!cast.birthday.isNullOrEmpty()) {
            val formattedBirthday = dateConversion(cast.birthday)
            Text(text = "Born: $formattedBirthday", fontSize = 14.sp)
        }

        if (!cast.place_of_birth.isNullOrEmpty()) {
            Text(text = "From: ${cast.place_of_birth}", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Cast person biography
        cast.biography?.let {
            Text(text = "Biography", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = it, fontSize = 14.sp)
        }


        // Other cast information


    }
}

@Composable
fun dateConversion(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val parsedDate = inputFormat.parse(date)
    return outputFormat.format(parsedDate ?: "")
}
