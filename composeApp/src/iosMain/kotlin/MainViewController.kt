import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import tech.pacia.opencaching.MyApplicationTheme
import tech.pacia.opencaching.App

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        MyApplicationTheme {
            App()
        }
    }
}