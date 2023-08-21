package com.example.movieproject.ui.design.favorites

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import com.example.jetsnack.ui.utils.mirroringIcon
import com.example.movieproject.R
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.movieproject.ui.components.OrderLine
import com.example.movieproject.ui.theme.MovieTheme

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CartPreview() {
    MovieTheme {
        Favorites(
            orderLines = arrayListOf()
        )
    }
}

@Composable
fun Favorites(

    orderLines: ArrayList<OrderLine>,
    modifier: Modifier = Modifier
) {
        Box {
            CartContent(
                orderLines = orderLines,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

}
@Composable
fun CartContent(
    orderLines: List<OrderLine>,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    val snackCountFormattedString = remember(orderLines.size, resources) {
        resources.getQuantityString(
            R.plurals.cart_order_count,
            orderLines.size, orderLines.size
        )
    }
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = "Courses",
                style = MaterialTheme.typography.h6,
                color = MovieTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            IconButton(
                onClick = { /* todo */ },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = mirroringIcon(
                        ltrIcon = Icons.Outlined.ArrowForward,
                        rtlIcon = Icons.Outlined.ArrowBack
                    ),
                    tint = MovieTheme.colors.brand,
                    contentDescription = null
                )
            }
        }
        LazyColumn(modifier) {
            item {
            }
            items(orderLines) { orderLine ->
                CartItem(
                    orderLine = orderLine,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun CartItem(
    orderLine: OrderLine,
    modifier: Modifier = Modifier
) {
    val movie = orderLine.movie
    ConstraintLayout(
         modifier = modifier
            .fillMaxWidth()
            .background(MovieTheme.colors.uiBackground)
            //.padding(horizontal = 24.dp)
            .padding(start = 24.dp)
            .padding(end = 16.dp)
    ) {
        val (divider, image, name, tag, priceSpacer, price, remove, goInside) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)
        /*
        SnackImage(
            imageUrl = movie.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )*/
        Text(
            text = "Concept of Programming Languages",
            style = MaterialTheme.typography.body2,
            color = MovieTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 15.dp,
                    end = goInside.start,
                    endMargin = 5.dp,
                    bias = 0f
                )
                top.linkTo(image.top)
                bottom.linkTo(tag.bottom)
                width = Dimension.fillToConstraints
            }
        )
        IconButton(
            onClick = { /* todo */ },
            modifier = Modifier
                .size(20.dp)
                .constrainAs(goInside) {
                    linkTo(
                        start = name.end,
                        startMargin = 16.dp,
                        end = parent.end,
                        endMargin = 0.dp,
                        bias = 0f
                    )
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_local_movies_24),
                tint = MovieTheme.colors.brand,
                contentDescription = "Go Inside"
            )
        }
        Text(
            text = "CME 3202",
            style = MaterialTheme.typography.body1,
            color = MovieTheme.colors.textHelp,
            modifier = Modifier
                .padding(top = 4.dp)
                .constrainAs(tag) {
                    linkTo(
                        start = image.end,
                        startMargin = 16.dp,
                        end = parent.end,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                    top.linkTo(name.bottom)
                    bottom.linkTo(image.bottom)
                }
        )
        Spacer(
            Modifier
                .height(8.dp)
                .constrainAs(priceSpacer) {
                    linkTo(top = tag.bottom, bottom = price.top)
                }
        )
    }
}

