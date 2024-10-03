package tech.pacia.opencaching.features

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeRoute(
    topLevelNavController: NavHostController,
    homeNavController: NavHostController,
) {
    HomeScreen(
        topLevelNavController = topLevelNavController,
        homeNavController = homeNavController,
    )
}
