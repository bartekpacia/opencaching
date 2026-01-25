package tech.pacia.opencaching.data

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFTypeRefVar
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.darwin.OSStatus
import tech.pacia.okapi.client.auth.AccessToken

private const val SERVICE_NAME = "tech.pacia.opencaching.oauth"
private const val ACCESS_TOKEN_ACCOUNT = "access_token"
private const val ACCESS_TOKEN_SECRET_ACCOUNT = "access_token_secret"

/**
 * iOS implementation of [TokenStorage] using the Keychain.
 *
 * The Keychain provides secure, encrypted storage for sensitive data on iOS.
 */
actual class TokenStorage {
    actual suspend fun saveAccessToken(accessToken: AccessToken) {
        saveToKeychain(ACCESS_TOKEN_ACCOUNT, accessToken.token)
        saveToKeychain(ACCESS_TOKEN_SECRET_ACCOUNT, accessToken.tokenSecret)
    }

    actual suspend fun getAccessToken(): AccessToken? {
        val token = readFromKeychain(ACCESS_TOKEN_ACCOUNT)
        val secret = readFromKeychain(ACCESS_TOKEN_SECRET_ACCOUNT)
        return if (token != null && secret != null) {
            AccessToken(token = token, tokenSecret = secret)
        } else {
            null
        }
    }

    actual suspend fun clearTokens() {
        deleteFromKeychain(ACCESS_TOKEN_ACCOUNT)
        deleteFromKeychain(ACCESS_TOKEN_SECRET_ACCOUNT)
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun saveToKeychain(account: String, value: String) {
        // First delete any existing value
        deleteFromKeychain(account)

        val nsString = NSString.create(string = value)
        val data = nsString.dataUsingEncoding(NSUTF8StringEncoding) ?: return

        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassGenericPassword,
            kSecAttrService to SERVICE_NAME,
            kSecAttrAccount to account,
            kSecValueData to data,
        )

        @Suppress("UNCHECKED_CAST")
        val cfQuery = CFBridgingRetain(query) as CFDictionaryRef
        SecItemAdd(cfQuery, null)
        CFBridgingRelease(cfQuery)
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun readFromKeychain(account: String): String? {
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassGenericPassword,
            kSecAttrService to SERVICE_NAME,
            kSecAttrAccount to account,
            kSecReturnData to true,
            kSecMatchLimit to kSecMatchLimitOne,
        )

        @Suppress("UNCHECKED_CAST")
        val cfQuery = CFBridgingRetain(query) as CFDictionaryRef

        return memScoped {
            val result = alloc<CFTypeRefVar>()
            val status: OSStatus = SecItemCopyMatching(cfQuery, result.ptr)
            CFBridgingRelease(cfQuery)

            if (status == errSecSuccess) {
                val data = CFBridgingRelease(result.value) as? NSData
                data?.let {
                    NSString.create(data = it, encoding = NSUTF8StringEncoding)?.toString()
                }
            } else {
                null
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun deleteFromKeychain(account: String) {
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassGenericPassword,
            kSecAttrService to SERVICE_NAME,
            kSecAttrAccount to account,
        )

        @Suppress("UNCHECKED_CAST")
        val cfQuery = CFBridgingRetain(query) as CFDictionaryRef
        SecItemDelete(cfQuery)
        CFBridgingRelease(cfQuery)
    }
}
