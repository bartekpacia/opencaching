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

        setContent {
            App(
                signInViewModel = signInViewModel,
                onOpenBrowser = { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                },
            )
        }
    }
}
