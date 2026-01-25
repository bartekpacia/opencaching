package tech.pacia.opencaching

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import tech.pacia.opencaching.data.AuthRepository
import tech.pacia.opencaching.data.TokenStorage
import tech.pacia.opencaching.features.signin.SignInViewModel
import java.awt.Desktop
import java.net.URI

fun main() = application {
    val tokenStorage = TokenStorage()
    val authRepository = AuthRepository(tokenStorage)
    val signInViewModel = SignInViewModel(authRepository)

    Window(
        onCloseRequest = ::exitApplication,
        title = "opencaching",
    ) {
        App(
            signInViewModel = signInViewModel,
            onOpenBrowser = { url ->
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(URI(url))
                }
            },
        )
    }
}
