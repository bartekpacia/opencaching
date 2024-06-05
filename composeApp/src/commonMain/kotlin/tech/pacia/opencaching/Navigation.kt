package tech.pacia.opencaching

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.pacia.opencaching.features.geocache.GeocacheRoute
import tech.pacia.opencaching.features.geocache.description.GeocacheDescriptionRoute
import tech.pacia.opencaching.features.map.MapRoute
import tech.pacia.opencaching.features.signin.SignInRoute

private object Destinations {
    const val SIGN_IN_ROUTE = "signin"
    const val MAP_ROUTE = "map/"
    const val GEOCACHE_ROUTE = "geocache/{code}"
    const val GEOCACHE_DESCRIPTION_ROUTE = "geocache/{code}/description"
}

@Composable
fun OpencachingNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SIGN_IN_ROUTE,
    ) {
        composable(Destinations.SIGN_IN_ROUTE) {
            SignInRoute(
                onNavigateToMap = { navController.navigate(Destinations.MAP_ROUTE) },
            )
        }

        composable(Destinations.MAP_ROUTE) {
            MapRoute(
                onNavigateToGeocache = { cache ->
                    navController.navigate("geocache/${cache.code}")
                },
            )
        }

        composable(Destinations.GEOCACHE_ROUTE) { backstackEntry ->
            val code = backstackEntry.arguments?.getString("code") ?: "empty"
            GeocacheRoute(
                code = code,
                onNavUp = { navController.popBackStack() },
                onNavigateToDescription = {
                    navController.navigate("geocache/$code/description?html=XDXD")
                },
            )
        }

        composable(Destinations.GEOCACHE_DESCRIPTION_ROUTE) { backstackEntry ->
            val html = backstackEntry.arguments?.getString("html") ?: "empty"
            GeocacheDescriptionRoute(
                html = html,
                onNavUp = { navController.popBackStack() },
            )
        }
    }
}
