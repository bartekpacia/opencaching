package tech.pacia.opencaching

import androidx.compose.runtime.Composable
import tech.pacia.opencaching.features.signin.SignInViewModel
import tech.pacia.opencaching.ui.theme.OpencachingTheme

@Composable
fun App(
    signInViewModel: SignInViewModel,
    onOpenBrowser: (url: String) -> Unit,
) {
    OpencachingTheme {
        OpencachingNavHost(
            signInViewModel = signInViewModel,
            onOpenBrowser = onOpenBrowser,
        )
    }
}
