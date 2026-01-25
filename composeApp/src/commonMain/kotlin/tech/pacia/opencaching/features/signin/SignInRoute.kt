package tech.pacia.opencaching.features.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun SignInRoute(
    viewModel: SignInViewModel,
    onNavigateToMap: () -> Unit,
    onOpenBrowser: (url: String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            is SignInState.OpenBrowser -> {
                onOpenBrowser((state as SignInState.OpenBrowser).url)
                viewModel.onBrowserOpened()
            }
            is SignInState.Success -> {
                onNavigateToMap()
            }
            else -> {}
        }
    }

    SignInScreen(
        state = state,
        onSignInClicked = { viewModel.startSignIn() },
        onSignInSkipped = onNavigateToMap,
        onDismissError = { viewModel.dismissError() },
    )
}
