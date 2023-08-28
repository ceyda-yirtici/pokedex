package com.example.movieproject.ui.design.favorites

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movieproject.ui.theme.MovieTheme


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CartPreview() {
    MovieTheme {
        FavoriteList(
            list = favList.getList(),
            navController = navigateController.getItem()
        )
    }
}

object favList {
    lateinit var orderLines : List<String>
    fun getList(): List<String> {
        return orderLines
    }
    fun setList(orderLines:  List<String>) {
        this.orderLines = orderLines
    }
}

object navigateController {
    lateinit var navController : NavController
    fun getItem(): NavController {
        return navController
    }
    fun setItem( navController: NavController)  {
        this.navController = navController
    }

}
/*
@Composable
private fun Favorites(
    onSnackClick: (Long) -> Unit,
    orderLines: ArrayList<OrderLine>,
    modifier: Modifier = Modifier
) {
        Box {
            CartContent(
                orderLines = orderLines,
                onSnackClick = onSnackClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

}*/


@Composable
fun FavoriteList(list: List<String>, navController: NavController) {
    favList.setList(list)
    navigateController.setItem(navController)
    LazyColumn {
        items(list) { item ->
            Text(text = item, modifier = Modifier.padding(8.dp))
        }
    }
}
/*
@Composable
fun Favorites(
    args: FavoritesFragmentArgs,
    orderLines: ArrayList<OrderLine>
) {
    val onSnackClick: (Long) -> Unit =  { id -> args.onSnackSelected(id, args.from) }
    val onNavigateToRoute: ((String) -> Unit) = args.navigateToRoute
    val modifier: Modifier = args.modifier
    list.setList(orderLines)
    val jetsnackScaffoldState = rememberJetsnackScaffoldState()
    JetsnackScaffold(
        bottomBar = {
            MovieBottomBar(
                tabs = NavBarSections.values(),
                currentRoute = NavBarSections.FAVORITES.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.systemBarsPadding(),
            )
        },
        scaffoldState = jetsnackScaffoldState.scaffoldState,
        modifier = modifier
    ) { paddingValues ->
        Favorites(
            orderLines = orderLines,
            onSnackClick = onSnackClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun CartContent(
    orderLines: List<OrderLine>,
    onSnackClick: (Long) -> Unit,
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
                    onSnackClick = onSnackClick,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun CartItem(
    orderLine: OrderLine,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = orderLine.movie
    ConstraintLayout(
         modifier = modifier
            .fillMaxWidth()
             .clickable { onSnackClick(movie.id.toLong()) }
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
*/
