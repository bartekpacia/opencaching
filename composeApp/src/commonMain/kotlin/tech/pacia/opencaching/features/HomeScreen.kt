package tech.pacia.opencaching.features


import androidx.compose.foundation.layout.padding
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
import tech.pacia.opencaching.Destinations
import tech.pacia.opencaching.features.map.MapRoute
import tech.pacia.opencaching.features.profile.ProfileRoute

private object HomeNavDestinations {
//    @Serializable
//    object HomeMapTab
//
//    @Serializable
//    object HomeProfileTab

    val HomeMapTab = "/map"
    val HomeProfileTab = "/profile"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val currentDestination = navController.currentDestination?.route

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Home") },
            )
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeNavDestinations.HomeMapTab,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = HomeNavDestinations.HomeMapTab) {
                    MapRoute(
                        onNavigateToGeocache = { cache ->
                            navController.navigate(Destinations.Geocache(cacheCode = cache.code))
                        },
                    )
                }

                composable(route = HomeNavDestinations.HomeProfileTab) {
                    ProfileRoute(
                        onNavigateToGeocache = { cache ->
                            navController.navigate(Destinations.Geocache(cacheCode = cache.code))
                        },
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = HomeNavDestinations.HomeMapTab == currentDestination,
                    onClick = {
                        navController.navigate(HomeNavDestinations.HomeMapTab)
                    },
                    icon = { Icon(Icons.Rounded.Map, contentDescription = "Map") },
                    label = { Text("Map") },
                )

                NavigationBarItem(
                    selected = HomeNavDestinations.HomeProfileTab == currentDestination,
                    onClick = {
                        navController.navigate(HomeNavDestinations.HomeProfileTab)
                    },
                    icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                )
            }
        }
    )
}