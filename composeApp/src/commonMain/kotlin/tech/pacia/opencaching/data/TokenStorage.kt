package tech.pacia.opencaching.data

import tech.pacia.okapi.client.auth.AccessToken

/**
 * Platform-specific secure storage for OAuth tokens.
 */
expect class TokenStorage {
    /**
     * Saves the access token to secure storage.
     */
    suspend fun saveAccessToken(accessToken: AccessToken)

    /**
     * Retrieves the stored access token, or null if none exists.
     */
    suspend fun getAccessToken(): AccessToken?

    /**
     * Clears all stored tokens.
     */
    suspend fun clearTokens()
}
