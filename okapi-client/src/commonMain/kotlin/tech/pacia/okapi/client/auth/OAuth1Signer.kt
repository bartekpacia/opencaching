package tech.pacia.okapi.client.auth

import org.kotlincrypto.macs.hmac.sha1.HmacSHA1
import kotlin.io.encoding.Base64
import kotlin.time.Clock

/**
 * Generates OAuth 1.0a signatures for HTTP requests.
 *
 * Implements the signature process defined in RFC 5849 Section 3.4.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.4">RFC 5849 Section 3.4 - Signature</a>
 */
public class OAuth1Signer {
    /**
     * Characters that don't need percent-encoding per RFC 5849.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.6">RFC 5849 Section 3.6 - Percent Encoding</a>
     */
    private val UNRESERVED_CHARS = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('-', '.', '_', '~')


    /**
     * Returns OAuth protocol parameters that are added to the request to sign it.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.1">RFC 5849 Section 3.1 - Making Requests</a>
     */
    public fun sign(
        httpMethod: String,
        baseUrl: String,
        queryParameters: Map<String, String> = emptyMap(),
        consumerCredentials: ConsumerCredentials,
        token: String? = null,
        tokenSecret: String? = null,
        nonce: String = generateNonce(),
        timestamp: Long = currentTimestamp(),
    ): Map<String, String> {
        val oauthParameters = mutableMapOf(
            "oauth_consumer_key" to consumerCredentials.key,
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to timestamp.toString(),
            "oauth_nonce" to nonce,
            "oauth_version" to "1.0",
        )

        if (token != null) {
            oauthParameters["oauth_token"] = token
        }

        val signature = generateSignature(
            httpMethod = httpMethod,
            baseUrl = baseUrl,
            queryParameters = queryParameters,
            oauthParameters = oauthParameters,
            consumerSecret = consumerCredentials.secret,
            tokenSecret = tokenSecret,
        )

        oauthParameters["oauth_signature"] = signature
        return oauthParameters
    }

    /**
     * Generates the oauth_signature value using HMAC-SHA1.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.4.2">RFC 5849 Section 3.4.2 - HMAC-SHA1</a>
     */
    internal fun generateSignature(
        httpMethod: String,
        baseUrl: String,
        queryParameters: Map<String, String>,
        oauthParameters: Map<String, String>,
        consumerSecret: String,
        tokenSecret: String?,
    ): String {
        val signatureBaseString = buildSignatureBaseString(
            httpMethod = httpMethod,
            baseUrl = baseUrl,
            queryParameters = queryParameters,
            oauthParameters = oauthParameters,
        )

        val signingKey = buildSigningKey(consumerSecret, tokenSecret)

        val hmac = HmacSHA1(signingKey.encodeToByteArray())
        val signatureBytes = hmac.doFinal(signatureBaseString.encodeToByteArray())

        return Base64.encode(signatureBytes)
    }

    /**
     * Builds the _signature base string_ per RFC 5849 Section 3.4.1.
     *
     * The signature base string is constructed by concatenating:
     * 1. The uppercase HTTP method
     * 2. An "&" character
     * 3. The percent-encoded base URL
     * 4. An "&" character
     * 5. The percent-encoded normalized request parameters
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.4.1">RFC 5849 Section 3.4.1 - Signature Base String</a>
     */
    internal fun buildSignatureBaseString(
        httpMethod: String,
        baseUrl: String,
        queryParameters: Map<String, String>,
        oauthParameters: Map<String, String>,
    ): String {
        val normalizedParameters = normalizeParameters(queryParameters + oauthParameters)

        return buildString {
            append(httpMethod.uppercase())
            append("&")
            append(percentEncode(baseUrl))
            append("&")
            append(percentEncode(normalizedParameters))
        }
    }


    /**
     * Normalizes parameters per RFC 5849 Section 3.4.1.3.2.
     *
     * Parameters are:
     * 1. Percent-encoded
     * 2. Sorted by name, then by value
     * 3. Concatenated with "&"
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.4.1.3.2">RFC 5849 Section 3.4.1.3.2 - Parameters Normalization</a>
     */
    private fun normalizeParameters(parameters: Map<String, String>): String {
        return parameters.entries
            .map { (key, value) -> percentEncode(key) to percentEncode(value) }
            .sortedWith(compareBy({ it.first }, { it.second }))
            .joinToString("&") { (key, value) -> "$key=$value" }
    }

    /**
     * Builds the signing key per RFC 5849 Section 3.4.2.
     *
     * The signing key is the concatenation of:
     * 1. The percent-encoded consumer secret
     * 2. An "&" character
     * 3. The percent-encoded token secret (or empty string if not available)
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.4.2">RFC 5849 Section 3.4.2 - HMAC-SHA1</a>
     */
    internal fun buildSigningKey(consumerSecret: String, tokenSecret: String?): String {
        return "${percentEncode(consumerSecret)}&${percentEncode(tokenSecret ?: "")}"
    }


    /**
     * Percent-encodes a string per RFC 5849 Section 3.6.
     *
     * All characters except unreserved characters (A-Z, a-z, 0-9, '-', '.', '_', '~')
     * are percent-encoded.
     *
     * Note: We can't use Ktor's `encodeURLQueryComponent()` because OAuth 1.0a requires
     * RFC 3986 percent-encoding, which encodes spaces as `%20`. Standard URL query encoding
     * encodes spaces as `+`, which would produce an invalid signature.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.6">RFC 5849 Section 3.6 - Percent Encoding</a>
     */
    public fun percentEncode(value: String): String {
        return buildString {
            for (char in value) {
                if (char in UNRESERVED_CHARS) {
                    append(char)
                } else {
                    // Encode each byte of the UTF-8 representation
                    for (byte in char.toString().encodeToByteArray()) {
                        append('%')
                        append(byte.toInt().and(0xFF).toString(16).uppercase().padStart(2, '0'))
                    }
                }
            }
        }
    }


    /**
     * Generates a 32-character random nonce for OAuth requests.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.3">RFC 5849 Section 3.3 - Nonce and Timestamp</a>
     */
    private fun generateNonce(): String {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..32).map { chars.random() }.joinToString("")
    }

    /**
     * Returns the current number of seconds since Unix epoch.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5849#section-3.3">RFC 5849 Section 3.3 - Nonce and Timestamp</a>
     */
    private fun currentTimestamp(): Long = Clock.System.now().epochSeconds
}
