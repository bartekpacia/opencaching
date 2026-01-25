package tech.pacia.opencaching

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import tech.pacia.opencaching.data.AuthRepository
import tech.pacia.opencaching.data.TokenStorage
import tech.pacia.opencaching.features.signin.SignInViewModel

class MainActivity : ComponentActivity() {
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenStorage = TokenStorage(applicationContext)
        val authRepository = AuthRepository(tokenStorage)
        signInViewModel = SignInViewModel(authRepository)

        handleOAuthCallback(intent)

        setContent {
            App(
                signInViewModel = signInViewModel,
                onOpenBrowser = { url ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                },
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleOAuthCallback(intent)
    }

    private fun handleOAuthCallback(intent: Intent) {
        val uri = intent.data ?: return
        if (uri.scheme == "opencaching" && uri.host == "oauth" && uri.path == "/callback") {
            val verifier = uri.getQueryParameter("oauth_verifier")
            if (verifier != null) {
                signInViewModel.handleOAuthCallback(verifier)
            }
        }
    }
}
