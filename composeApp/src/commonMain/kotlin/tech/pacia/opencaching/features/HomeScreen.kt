package tech.pacia.opencaching.features

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import tech.pacia.opencaching.TopLevelDestinations
import tech.pacia.opencaching.features.map.MapRoute
import tech.pacia.opencaching.features.profile.ProfileRoute

private object HomeNavDestinations {
    @Serializable
    data object HomeMapTab

    @Serializable
    data object HomeProfileTab
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    topLevelNavController: NavHostController,
    homeNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val currentDestination = homeNavController.currentDestination?.route

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Home") },
            )
        },
        content = {
            NavHost(
                navController = homeNavController,
                startDestination = HomeNavDestinations.HomeMapTab,
            ) {
                composable<HomeNavDestinations.HomeMapTab> {
                    MapRoute(
                        onNavigateToGeocache = { cache ->
                            topLevelNavController.navigate(TopLevelDestinations.Geocache(cacheCode = cache.code))
                        },
                    )
                }

                composable<HomeNavDestinations.HomeProfileTab> {
                    ProfileRoute(
                        onNavigateToGeocache = { cache ->
                            topLevelNavController.navigate(TopLevelDestinations.Geocache(cacheCode = cache.code))
                        },
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = HomeNavDestinations.HomeMapTab.toString() == currentDestination,
                    onClick = {
                        homeNavController.navigate(HomeNavDestinations.HomeMapTab)
                    },
                    icon = { Icon(Icons.Rounded.Map, contentDescription = "Map") },
                    label = { Text("Map") },
                )

                NavigationBarItem(
                    selected = HomeNavDestinations.HomeProfileTab.toString() == currentDestination,
                    onClick = { homeNavController.navigate(HomeNavDestinations.HomeProfileTab) },
                    icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                )
            }
        },
    )
}
