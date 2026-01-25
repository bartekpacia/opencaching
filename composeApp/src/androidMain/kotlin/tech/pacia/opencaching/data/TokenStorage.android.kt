package tech.pacia.opencaching.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import tech.pacia.okapi.client.auth.AccessToken

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "oauth_tokens")

private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
private val ACCESS_TOKEN_SECRET_KEY = stringPreferencesKey("access_token_secret")

/**
 * Android implementation of [TokenStorage] using DataStore Preferences.
 */
// TODO: Store in an encrypted storage like EncryptedSharedPreferences or Tink.
actual class TokenStorage(private val context: Context) {
    actual suspend fun saveAccessToken(accessToken: AccessToken) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken.token
            preferences[ACCESS_TOKEN_SECRET_KEY] = accessToken.tokenSecret
        }
    }

    actual suspend fun getAccessToken(): AccessToken? {
        return context.dataStore.data.map { preferences ->
            val token = preferences[ACCESS_TOKEN_KEY]
            val secret = preferences[ACCESS_TOKEN_SECRET_KEY]
            if (token != null && secret != null) {
                AccessToken(token = token, tokenSecret = secret)
            } else {
                null
            }
        }.firstOrNull()
    }

    actual suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(ACCESS_TOKEN_SECRET_KEY)
        }
    }
}
