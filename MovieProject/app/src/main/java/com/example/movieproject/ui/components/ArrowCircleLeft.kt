package com.example.movieproject.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieproject.ui.theme.MovieTheme


@Preview
@Composable
fun ArrowPreview(){
    MovieTheme {
        rememberArrowCircleLeft()
    }
}
@Composable
fun rememberCircle(): ImageVector {

    val outLineColor = MovieTheme.colors.uiBackground
    return remember {
        ImageVector.Builder(
            name = "circle",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(outLineColor),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 36.375f)
                quadToRelative(-3.375f, 0f, -6.375f, -1.292f)
                quadToRelative(-3f, -1.291f, -5.208f, -3.521f)
                quadToRelative(-2.209f, -2.229f, -3.5f, -5.208f)
                quadTo(3.625f, 23.375f, 3.625f, 20f)
                quadToRelative(0f, -3.417f, 1.292f, -6.396f)
                quadToRelative(1.291f, -2.979f, 3.521f, -5.208f)
                quadToRelative(2.229f, -2.229f, 5.208f, -3.5f)
                reflectiveQuadTo(20f, 3.625f)
                quadToRelative(3.417f, 0f, 6.396f, 1.292f)
                quadToRelative(2.979f, 1.291f, 5.208f, 3.5f)
                quadToRelative(2.229f, 2.208f, 3.5f, 5.187f)
                reflectiveQuadTo(36.375f, 20f)
                quadToRelative(0f, 3.375f, -1.292f, 6.375f)
                quadToRelative(-1.291f, 3f, -3.5f, 5.208f)
                quadToRelative(-2.208f, 2.209f, -5.187f, 3.5f)
                quadToRelative(-2.979f, 1.292f, -6.396f, 1.292f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberArrowCircleLeft(): ImageVector {

    val outLineColor = MovieTheme.colors.iconSecondary
    return remember {
        ImageVector.Builder(
            name = "arrow_circle_left",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(outLineColor),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19.125f, 25.292f)
                quadToRelative(0.333f, 0.375f, 0.896f, 0.354f)
                quadToRelative(0.562f, -0.021f, 0.896f, -0.396f)
                quadToRelative(0.416f, -0.417f, 0.416f, -0.938f)
                quadToRelative(0f, -0.52f, -0.416f, -0.895f)
                lineToRelative(-2.084f, -2.125f)
                horizontalLineToRelative(6.209f)
                quadToRelative(0.541f, 0f, 0.916f, -0.375f)
                reflectiveQuadToRelative(0.375f, -0.917f)
                quadToRelative(0f, -0.542f, -0.395f, -0.938f)
                quadToRelative(-0.396f, -0.395f, -0.938f, -0.395f)
                horizontalLineToRelative(-6.167f)
                lineToRelative(2.125f, -2.125f)
                quadToRelative(0.375f, -0.334f, 0.354f, -0.896f)
                quadToRelative(-0.02f, -0.563f, -0.395f, -0.896f)
                quadToRelative(-0.417f, -0.417f, -0.938f, -0.417f)
                quadToRelative(-0.521f, 0f, -0.896f, 0.417f)
                lineToRelative(-4.333f, 4.333f)
                quadToRelative(-0.375f, 0.375f, -0.375f, 0.917f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                close()
                moveTo(20f, 36.375f)
                quadToRelative(-3.458f, 0f, -6.458f, -1.25f)
                reflectiveQuadToRelative(-5.209f, -3.458f)
                quadToRelative(-2.208f, -2.209f, -3.458f, -5.209f)
                quadToRelative(-1.25f, -3f, -1.25f, -6.458f)
                reflectiveQuadToRelative(1.25f, -6.437f)
                quadToRelative(1.25f, -2.98f, 3.458f, -5.188f)
                quadToRelative(2.209f, -2.208f, 5.209f, -3.479f)
                quadToRelative(3f, -1.271f, 6.458f, -1.271f)
                reflectiveQuadToRelative(6.438f, 1.271f)
                quadToRelative(2.979f, 1.271f, 5.187f, 3.479f)
                reflectiveQuadToRelative(3.479f, 5.188f)
                quadToRelative(1.271f, 2.979f, 1.271f, 6.437f)
                reflectiveQuadToRelative(-1.271f, 6.458f)
                quadToRelative(-1.271f, 3f, -3.479f, 5.209f)
                quadToRelative(-2.208f, 2.208f, -5.187f, 3.458f)
                quadToRelative(-2.98f, 1.25f, -6.438f, 1.25f)
                close()
                moveToRelative(0f, -2.625f)
                quadToRelative(5.833f, 0f, 9.792f, -3.958f)
                quadTo(33.75f, 25.833f, 33.75f, 20f)
                reflectiveQuadToRelative(-3.958f, -9.792f)
                quadTo(25.833f, 6.25f, 20f, 6.25f)
                reflectiveQuadToRelative(-9.792f, 3.958f)
                quadTo(6.25f, 14.167f, 6.25f, 20f)
                reflectiveQuadToRelative(3.958f, 9.792f)
                quadTo(14.167f, 33.75f, 20f, 33.75f)
                close()
                moveTo(20f, 20f)
                close()
            }
        }.build()
    }
}





