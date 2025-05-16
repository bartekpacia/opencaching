import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import tech.pacia.opencaching.App

@OptIn(ExperimentalComposeApi::class)
@Suppress("FunctionNaming", "Unused")
fun MainViewController(): UIViewController {
    return ComposeUIViewController(
        content = { App() },
    )
}
