import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import tech.pacia.opencaching.App
import tech.pacia.opencaching.features.signin.SignInViewModel

@Suppress("FunctionName", "Unused")
fun MainViewController(
    signInViewModel: SignInViewModel,
): UIViewController {
    return ComposeUIViewController(
        content = {
            App(
                signInViewModel = signInViewModel,
                onOpenBrowser = { url ->
                    val nsUrl = NSURL.URLWithString(url)
                        ?: throw IllegalArgumentException("Invalid URL: $url")

                    UIApplication.sharedApplication.openURL(
                        url = nsUrl,
                        options = emptyMap<Any?, Any>(),
                        completionHandler = null,
                    )
                },
            )
        },
    )
}
