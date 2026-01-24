package tech.pacia.okapi.client.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

/**
 * Service for OAuth 1.0a authentication with OKAPI.
 *
 * Implements the OAuth 1.0a authorization flow as defined in RFC 5849:
 * 1. Get request token via [getRequestToken]
 * 2. Redirect user to authorization URL via [getAuthorizationUrl]
 * 3. Exchange verifier for access token via [getAccessToken]
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-2">RFC 5849 Section 2 - Redirection-Based Authorization</a>
 * @see <a href="https://opencaching.pl/okapi/services/oauth.html">OKAPI OAuth Documentation</a>
 */
public class OAuthService(
    private val httpClient: HttpClient,
    private val apiUrl: String,
    private val consumerCredentials: ConsumerCredentials,
    private val callbackUrl: String,
) {
    private val signer = OAuth1Signer()

    /**
     * Step 1: Gets a request token (the "temporary credentials") from the server.
     *
     * The request token is used to identify the authorization request
     * when directing the user to the authorization page.
     *
     * @return Request token containing temporary credentials
     * @throws OAuthException if the request fails
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-2.1">RFC 5849 Section 2.1 - Temporary Credentials</a>
     * @see <a href="https://opencaching.pl/okapi/services/oauth/request_token.html">OKAPI request_token</a>
     */
    public suspend fun getRequestToken(): RequestToken {
        val url = "$apiUrl/oauth/request_token"

        val oauthParams = signer.sign(
            httpMethod = "GET",
            baseUrl = url,
            queryParameters = mapOf("oauth_callback" to callbackUrl),
            consumerCredentials = consumerCredentials,
        )

        val response = httpClient.get(url) {
            parameter("oauth_callback", callbackUrl)
            for ((key, value) in oauthParams) {
                parameter(key, value)
            }
        }

        if (!response.status.isSuccess()) {
            throw OAuthException("Failed to get request token: ${response.status}")
        }

        val body = response.bodyAsText()
        return parseRequestTokenResponse(body)
    }

    /**
     * Step 2: Builds the authorization URL where the user should be redirected.
     *
     * The user will be asked to authenticate with their Opencaching account
     * and grant this application access to it.
     * After authorization, the user is redirected to the callback URL with an oauth_verifier.
     *
     * @param requestToken The request token obtained from [getRequestToken]
     * @return URL to open in the user's browser
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-2.2">RFC 5849 Section 2.2 - Resource Owner Authorization</a>
     * @see <a href="https://opencaching.pl/okapi/services/oauth/authorize.html">OKAPI authorize</a>
     */
    public fun getAuthorizationUrl(requestToken: RequestToken): String {
        return "$apiUrl/oauth/authorize?oauth_token=${requestToken.token}"
    }

    /**
     * Step 3: Exchanges the request token and verifier for an access token.
     *
     * This completes the OAuth flow. The returned access token can be used
     * to make authenticated API calls on behalf of the user.
     *
     * @param requestToken The request token from step 1
     * @param verifier The oauth_verifier received in the callback after user authorization
     * @return Access token for authenticated API calls
     * @throws OAuthException if the exchange fails
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-2.3">RFC 5849 Section 2.3 - Token Credentials</a>
     * @see <a href="https://opencaching.pl/okapi/services/oauth/access_token.html">OKAPI access_token</a>
     */
    public suspend fun getAccessToken(
        requestToken: RequestToken,
        verifier: String,
    ): AccessToken {
        val url = "$apiUrl/oauth/access_token"

        val queryParams = mapOf("oauth_verifier" to verifier)

        val oauthParams = signer.sign(
            httpMethod = "GET",
            baseUrl = url,
            queryParameters = queryParams,
            consumerCredentials = consumerCredentials,
            token = requestToken.token,
            tokenSecret = requestToken.tokenSecret,
        )

        val response = httpClient.get(url) {
            parameter("oauth_verifier", verifier)
            oauthParams.forEach { (key, value) ->
                parameter(key, value)
            }
        }

        if (!response.status.isSuccess()) {
            throw OAuthException("Failed to get access token: ${response.status}")
        }

        val body = response.bodyAsText()
        return parseAccessTokenResponse(body)
    }

    /**
     * Parses the request token response.
     *
     * Response format: `oauth_token=xxx&oauth_token_secret=yyy&oauth_callback_confirmed=true`
     */
    private fun parseRequestTokenResponse(response: String): RequestToken {
        val params = parseFormUrlEncoded(response)

        val token = params["oauth_token"]
            ?: throw OAuthException("Missing oauth_token in response")
        val tokenSecret = params["oauth_token_secret"]
            ?: throw OAuthException("Missing oauth_token_secret in response")

        return RequestToken(token = token, tokenSecret = tokenSecret)
    }

    /**
     * Parses the access token response.
     *
     * Response format: `oauth_token=xxx&oauth_token_secret=yyy`
     */
    private fun parseAccessTokenResponse(response: String): AccessToken {
        val params = parseFormUrlEncoded(response)

        val token = params["oauth_token"]
            ?: throw OAuthException("Missing oauth_token in response")
        val tokenSecret = params["oauth_token_secret"]
            ?: throw OAuthException("Missing oauth_token_secret in response")

        return AccessToken(token = token, tokenSecret = tokenSecret)
    }

    /**
     * Parses a form-urlencoded response body into a map.
     */
    private fun parseFormUrlEncoded(body: String): Map<String, String> {
        return body.split("&")
            .filter { it.isNotBlank() }
            .associate { pair ->
                val (key, value) = pair.split("=", limit = 2)
                key to value
            }
    }
}

/**
 * Exception thrown when an OAuth operation fails.
 */
// TODO: Should probably extend RuntimeException?
public class OAuthException(message: String, cause: Throwable? = null) : Exception(message, cause)
