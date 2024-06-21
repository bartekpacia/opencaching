import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import tech.pacia.opencaching.App

@Suppress("FunctionNaming")
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        App()
    }
}
