package tech.pacia.opencaching.features.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignInRoute(
    onNavigateToMap: () -> Unit,
) {
    // val signInViewModel: SignInViewModel = viewModel(modelClass = SignInViewModel::class.java)
    val signInViewModel: SignInViewModel = viewModel { SignInViewModel() }

    val inProgress = signInViewModel.inProgress.collectAsState().value
    val hasError = signInViewModel.hasError.collectAsState().value

    SignInScreen(
        inProgress = inProgress,
        hasError = hasError,
        onDismissError = { signInViewModel.dismissError() },
        onSignInSubmitted = { username, password ->
            signInViewModel.signIn(username, password, onNavigateToMap)
        },
        onSignInSkipped = onNavigateToMap,
    )
}
