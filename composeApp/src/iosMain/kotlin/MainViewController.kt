import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.platform.AccessibilitySyncOptions
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import tech.pacia.opencaching.App

@OptIn(ExperimentalComposeApi::class)
@Suppress("FunctionNaming")
fun MainViewController(): UIViewController {
    return ComposeUIViewController(
        configure = { accessibilitySyncOptions = AccessibilitySyncOptions.Always(null) },
        content = { App() },
    )
}
