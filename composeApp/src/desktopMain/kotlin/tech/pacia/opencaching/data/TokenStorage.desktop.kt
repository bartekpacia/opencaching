package tech.pacia.opencaching.data

import tech.pacia.okapi.client.auth.AccessToken
import java.util.prefs.Preferences

private const val ACCESS_TOKEN_KEY = "access_token"
private const val ACCESS_TOKEN_SECRET_KEY = "access_token_secret"

/**
 * Desktop implementation of [TokenStorage] using Java Preferences API.
 *
 * Note: Java Preferences are stored in platform-specific locations:
 * - Windows: Registry
 * - macOS: ~/Library/Preferences/
 * - Linux: ~/.java/.userPrefs/
 *
 * This storage is NOT encrypted. For production apps, consider using
 * a proper secrets manager or encrypting values before storage.
 */
// TODO: Consider respecting XDG Base Directory Spec on Linux (instead of Java Preferences)
actual class TokenStorage {
    private val prefs: Preferences = Preferences.userRoot().node("tech/pacia/opencaching/oauth")

    actual suspend fun saveAccessToken(accessToken: AccessToken) {
        prefs.put(ACCESS_TOKEN_KEY, accessToken.token)
        prefs.put(ACCESS_TOKEN_SECRET_KEY, accessToken.tokenSecret)
        prefs.flush()
    }

    actual suspend fun getAccessToken(): AccessToken? {
        val token = prefs.get(ACCESS_TOKEN_KEY, null)
        val secret = prefs.get(ACCESS_TOKEN_SECRET_KEY, null)
        return if (token != null && secret != null) {
            AccessToken(token = token, tokenSecret = secret)
        } else {
            null
        }
    }

    actual suspend fun clearTokens() {
        prefs.remove(ACCESS_TOKEN_KEY)
        prefs.remove(ACCESS_TOKEN_SECRET_KEY)
        prefs.flush()
    }
}
