package tech.pacia.okapi.client.auth

/**
 * Consumer credentials issued by the OKAPI installation.
 * These identify your application and are used for all OAuth requests.
 *
 * In OAuth 1.0a terminology, these are the "client credentials".
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-1.1">RFC 5849 Section 1.1 - Roles</a>
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-2.1">RFC 5849 Section 2.1 - Temporary Credentials</a>
 */
public data class ConsumerCredentials(
    val key: String,
    val secret: String,
)

/**
 * Temporary credentials returned by the request_token endpoint.
 *
 * Used during the OAuth authorization flow to identify the authorization request.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-2.1">RFC 5849 Section 2.1 - Temporary Credentials</a>
 */
public data class RequestToken(
    val token: String,
    val tokenSecret: String,
)

/**
 * Token credentials (access token) returned after user authorization.
 *
 * Used to make authenticated API calls on behalf of the user.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-2.3">RFC 5849 Section 2.3 - Token Credentials</a>
 */
public data class AccessToken(
    val token: String,
    val tokenSecret: String,
)
