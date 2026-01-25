@file:Suppress("MatchingDeclarationName")

package tech.pacia.opencaching

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import tech.pacia.opencaching.features.HomeRoute
import tech.pacia.opencaching.features.geocache.GeocacheRoute
import tech.pacia.opencaching.features.geocache.activity.GeocacheActivityRoute
import tech.pacia.opencaching.features.geocache.description.GeocacheDescriptionRoute
import tech.pacia.opencaching.features.signin.SignInRoute
import tech.pacia.opencaching.features.signin.SignInViewModel

object TopLevelDestinations {
    @Serializable
    data object SignIn

    @Serializable
    data object Home

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
    signInViewModel: SignInViewModel,
    onOpenBrowser: (url: String) -> Unit,
) {
    val homeNavController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TopLevelDestinations.SignIn,
    ) {
        composable<TopLevelDestinations.SignIn> {
            SignInRoute(
                viewModel = signInViewModel,
                onNavigateToMap = {
                    navController.navigate(TopLevelDestinations.Home)
                },
                onOpenBrowser = onOpenBrowser,
            )
        }

        composable<TopLevelDestinations.Home> {
            HomeRoute(
                topLevelNavController = navController,
                homeNavController = homeNavController,
            )
        }

        composable<TopLevelDestinations.Geocache> { backstackEntry ->
            val route: TopLevelDestinations.Geocache = backstackEntry.toRoute()
            GeocacheRoute(
                code = route.cacheCode,
                onNavUp = { navController.popBackStack() },
                onNavigateToDescription = {
                    navController.navigate(TopLevelDestinations.GeocacheDescription(cacheCode = route.cacheCode))
                },
                onNavigateToActivity = {
                    navController.navigate(TopLevelDestinations.GeocacheActivity(cacheCode = route.cacheCode))
                },
            )
        }

        composable<TopLevelDestinations.GeocacheDescription> { backstackEntry ->
            val route: TopLevelDestinations.GeocacheDescription = backstackEntry.toRoute()
            GeocacheDescriptionRoute(
                code = route.cacheCode,
                onNavUp = { navController.popBackStack() },
            )
        }

        composable<TopLevelDestinations.GeocacheActivity> { backstackEntry ->
            val route: TopLevelDestinations.GeocacheActivity = backstackEntry.toRoute()
            GeocacheActivityRoute(
                code = route.cacheCode,
                onNavUp = { navController.popBackStack() },
            )
        }
    }
}
