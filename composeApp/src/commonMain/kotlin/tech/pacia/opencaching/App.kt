package tech.pacia.opencaching

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import tech.pacia.opencaching.features.geocache.GeocacheScreen
import tech.pacia.opencaching.features.map.MapScreen
import tech.pacia.opencaching.features.sign_in.SignInScreen
import tech.pacia.opencaching.navigation.GeocachePage
import tech.pacia.opencaching.navigation.MapPage
import tech.pacia.opencaching.navigation.NavigationStack
import tech.pacia.opencaching.navigation.Page
import tech.pacia.opencaching.navigation.SignInPage

private val navStack: NavigationStack<Page> = NavigationStack(SignInPage())

val LocalNavigationStack = compositionLocalOf<NavigationStack<Page>> {
    throw IllegalStateException("NavigationStack must be provided")
}

@Composable
fun App() {
    CompositionLocalProvider(LocalNavigationStack provides navStack) {
        AnimatedContent(
            targetState = navStack.lastWithIndex(),
        ) { (_, page) ->
            when (page) {
                is SignInPage -> { SignInScreen() }

                is MapPage -> { MapScreen() }

                is GeocachePage -> { GeocacheScreen(page.code) }
            }
        }
    }
}