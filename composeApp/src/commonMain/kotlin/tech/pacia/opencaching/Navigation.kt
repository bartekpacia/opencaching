package tech.pacia.opencaching

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import tech.pacia.opencaching.features.HomeScreen
import tech.pacia.opencaching.features.geocache.GeocacheRoute
import tech.pacia.opencaching.features.geocache.activity.GeocacheActivityRoute
import tech.pacia.opencaching.features.geocache.description.GeocacheDescriptionRoute
import tech.pacia.opencaching.features.signin.SignInRoute

object Destinations {
    @Serializable
    object SignIn

    @Serializable
    object Home

//    @Serializable
//    object HomeMapTab
//
//    @Serializable
//    object HomeProfileTab

    @Serializable
    data class Geocache(val cacheCode: String)

    @Serializable
    data class GeocacheDescription(val cacheCode: String)

    @Serializable
    data class GeocacheActivity(val cacheCode: String)
}

@Composable
fun OpencachingNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val homeNavController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.SignIn,
    ) {
        composable<Destinations.SignIn> {
            SignInRoute(
                onNavigateToMap = {
                    navController.navigate(Destinations.Home)
                },
            )
        }

        composable<Destinations.Home> {
            HomeScreen(
                navController = homeNavController,
            )
        }

        composable<Destinations.Geocache> { backstackEntry ->
            val route: Destinations.Geocache = backstackEntry.toRoute()
            GeocacheRoute(
                code = route.cacheCode,
                onNavUp = { navController.popBackStack() },
                onNavigateToDescription = {
                    navController.navigate(Destinations.GeocacheDescription(cacheCode = route.cacheCode))
                },
                onNavigateToActivity = {
                    navController.navigate(Destinations.GeocacheActivity(cacheCode = route.cacheCode))
                },
            )
        }

        composable<Destinations.GeocacheDescription> { backstackEntry ->
            val route: Destinations.GeocacheDescription = backstackEntry.toRoute()
            GeocacheDescriptionRoute(
                code = route.cacheCode,
                onNavUp = { navController.popBackStack() },
            )
        }

        composable<Destinations.GeocacheActivity> { backstackEntry ->
            val route: Destinations.GeocacheActivity = backstackEntry.toRoute()
            GeocacheActivityRoute(
                code = route.cacheCode,
                onNavUp = { navController.popBackStack() },
            )
        }
    }
}
