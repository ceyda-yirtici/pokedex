package com.example.movieproject


import android.app.Application
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.movieproject.ui.design.moviedetail.MovieDetail
import com.example.movieproject.ui.navigation.MainDestinations
import com.example.movieproject.ui.navigation.NavBarSections
import com.example.movieproject.ui.navigation.addHomeGraph
import com.example.movieproject.ui.navigation.rememberJetsnackNavController
import com.example.movieproject.ui.theme.MovieTheme
import dagger.hilt.android.HiltAndroidApp


@Composable
fun MovieApplication() {
    MovieTheme {
        val jetsnackNavController = rememberJetsnackNavController()
        NavHost(
            navController = jetsnackNavController.navController,
            startDestination = MainDestinations.POPULAR_MOVIES_ROUTE
        ) {
            jetsnackNavGraph(
                onSnackSelected = jetsnackNavController::navigateToSnackDetail,
                upPress = jetsnackNavController::upPress,
                onNavigateToRoute = jetsnackNavController::navigateToBottomBarRoute
            )
        }
    }
}
// view modelde data class olaeak tut tum live dataları
private fun NavGraphBuilder.jetsnackNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    navigation(
        route = MainDestinations.POPULAR_MOVIES_ROUTE,
        startDestination = NavBarSections.MOVIES.route
    ) {
        addHomeGraph()
    }
    composable(
        "${MainDestinations.MOVIE_DETAIL_ROUTE}/{${MainDestinations.MOVIE_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.MOVIE_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val movieId = arguments.getLong(MainDestinations.MOVIE_ID_KEY)
        MovieDetail(movieId, upPress)
    }
}