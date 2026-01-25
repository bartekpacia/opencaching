import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import tech.pacia.opencaching.App
import tech.pacia.opencaching.data.AuthRepository
import tech.pacia.opencaching.data.TokenStorage
import tech.pacia.opencaching.features.signin.SignInViewModel

@Suppress("FunctionName", "Unused")
fun MainViewController(): UIViewController {
    val tokenStorage = TokenStorage()
    val authRepository = AuthRepository(tokenStorage)
    val signInViewModel = SignInViewModel(authRepository)

    return ComposeUIViewController(
        content = {
            App(
                signInViewModel = signInViewModel,
                onOpenBrowser = { url ->
                    NSURL.URLWithString(url)?.let { nsUrl ->
                        UIApplication.sharedApplication.openURL(nsUrl)
                    }
                },
            )
        },
    )
}
