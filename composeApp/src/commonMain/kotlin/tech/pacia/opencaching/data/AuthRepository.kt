package tech.pacia.opencaching.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import opencaching.composeApp.BuildConfig
import tech.pacia.okapi.client.OpencachingClient
import tech.pacia.okapi.client.auth.ConsumerCredentials
import tech.pacia.okapi.client.auth.OAuthService
import tech.pacia.okapi.client.auth.RequestToken

private const val API_URL = "https://opencaching.pl/okapi/services"
private const val CALLBACK_URL = "opencaching://oauth/callback"

private val consumerCredentials = ConsumerCredentials(
    key = BuildConfig.CONSUMER_KEY,
    secret = BuildConfig.CONSUMER_SECRET,
)

private val defaultHttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

/**
 * Repository managing OAuth authentication state and flow.
 *
 * ### Usage
 *
 * 1. Call [startOAuthFlow] to get the authorization URL
 * 2. Open the URL in a browser for user to authorize
 * 3. Handle the callback and extract oauth_verifier
 * 4. Call [completeOAuthFlow] with the verifier
 */
class AuthRepository(
    private val tokenStorage: TokenStorage,
    private val httpClient: HttpClient = defaultHttpClient,
) {
    private val oauthService = OAuthService(
        httpClient = httpClient,
        apiUrl = API_URL,
        consumerCredentials = consumerCredentials,
        callbackUrl = CALLBACK_URL,
    )

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private var pendingRequestToken: RequestToken? = null

    /**
     * Initializes the repository by checking for stored tokens.
     * Call this on app startup.
     */
    suspend fun initialize() {
        val token = tokenStorage.getAccessToken()
        _isLoggedIn.value = token != null
    }

    /**
     * Starts the OAuth flow by obtaining a request token.
     *
     * @return The authorization URL to open in a browser
     */
    suspend fun startOAuthFlow(): String {
        val requestToken = oauthService.getRequestToken()
        pendingRequestToken = requestToken
        return oauthService.getAuthorizationUrl(requestToken)
    }

    /**
     * Completes the OAuth flow by exchanging the verifier for an access token.
     *
     * @param verifier The oauth_verifier from the callback URL
     * @throws IllegalStateException if no pending request token exists
     */
    suspend fun completeOAuthFlow(verifier: String) {
        val requestToken = pendingRequestToken
            ?: throw IllegalStateException("No pending OAuth flow. Call startOAuthFlow first.")

        val accessToken = oauthService.getAccessToken(requestToken, verifier)
        tokenStorage.saveAccessToken(accessToken)
        pendingRequestToken = null
        _isLoggedIn.value = true
    }

    /**
     * Creates an authenticated [OpencachingClient] using stored tokens.
     *
     * @return Authenticated client, or null if not logged in
     */
    suspend fun getAuthenticatedClient(): OpencachingClient? {
        val accessToken = tokenStorage.getAccessToken() ?: return null
        return OpencachingClient(
            httpClient = httpClient,
            consumerCredentials = consumerCredentials,
            accessToken = accessToken,
            apiUrl = API_URL,
        )
    }

    /**
     * Logs out by clearing stored tokens.
     */
    suspend fun logout() {
        tokenStorage.clearTokens()
        _isLoggedIn.value = false
    }
}
