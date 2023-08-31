package com.example.movieproject.ui.design.movies


import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movieproject.ui.components.ListItemSurface
import com.example.movieproject.ui.components.OrderLine
import com.example.movieproject.ui.theme.MovieTheme

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CartPreview() {
    MovieTheme {
        Movies(
            orderLines = emptyList(),
            onItemClick = {}
        )
    }
}

@Composable
fun Movies(

    orderLines: List<OrderLine>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItemSurface(modifier = modifier.fillMaxSize()) {
        Box {

        }
    }
}
