package tech.pacia.okapi.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import tech.pacia.okapi.client.auth.AccessToken
import tech.pacia.okapi.client.auth.ConsumerCredentials
import tech.pacia.okapi.client.auth.OAuth1Signer
import tech.pacia.okapi.client.models.BoundingBox
import tech.pacia.okapi.client.models.Error
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.okapi.client.models.Log
import tech.pacia.okapi.client.models.User

public class Library {
    public fun someLibraryMethod(): Boolean {
        return true
    }
}

public const val OKAPI_URL_PL: String = "https://opencaching.pl/okapi/services"

private val defaultHttpClient = HttpClient {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            },
        )
    }
}

/**
 * Client for the Opencaching OKAPI.
 *
 * @param httpClient HTTP client to use for requests
 * @param consumerCredentials Consumer credentials for the application
 * @param accessToken Optional access token for authenticated (Level 3) requests
 * @param apiUrl Base URL for the OKAPI installation
 *
 * @see <a href="https://opencaching.pl/okapi/">OKAPI Documentation</a>
 */
public class OpencachingClient public constructor(
    private val httpClient: HttpClient = defaultHttpClient,
    private val consumerCredentials: ConsumerCredentials,
    private val accessToken: AccessToken? = null,
    public val apiUrl: String = OKAPI_URL_PL,
) {
    private val signer = OAuth1Signer()

    /**
     * Whether this client has an access token for authenticated requests.
     */
    public val isAuthenticated: Boolean
        get() = accessToken != null

    /**
     * Adds authentication parameters to the request.
     *
     * For unauthenticated requests (Level 1), adds only consumer_key.
     * For authenticated requests (Level 3), adds OAuth signature.
     */
    private fun HttpRequestBuilder.addAuth(
        url: String,
        queryParams: Map<String, String> = emptyMap(),
    ) {
        if (accessToken != null) {
            val oauthParams = signer.sign(
                httpMethod = "GET",
                baseUrl = url,
                queryParameters = queryParams,
                consumerCredentials = consumerCredentials,
                token = accessToken.token,
                tokenSecret = accessToken.tokenSecret,
            )
            oauthParams.forEach { (key, value) ->
                parameter(key, value)
            }
        } else {
            parameter("consumer_key", consumerCredentials.key)
        }
    }
    /**
     * Calls [caches/shortcuts/search_and_retrieve](https://opencaching.pl/okapi/services/caches/shortcuts/search_and_retrieve.html) endpoint.
     */
    public suspend fun searchAndRetrieve(bbox: BoundingBox): Map<String, Geocache> {
        val url = "$apiUrl/caches/shortcuts/search_and_retrieve"
        val queryParams = mapOf(
            "search_method" to "services/caches/search/bbox",
            "search_params" to Json.encodeToString(mapOf("bbox" to bbox.toPipeFormat())),
            "retr_method" to "services/caches/geocaches",
            "retr_params" to Json.encodeToString(
                mapOf(
                    "fields" to Geocache.allParams,
                    "owner_fields" to User.allParams,
                ),
            ),
            "wrap" to "false",
        )

        val response = httpClient.get(url) {
            accept(Application.Json)
            queryParams.forEach { (key, value) -> parameter(key, value) }
            addAuth(url, queryParams)
        }

        println(response.bodyAsText())

        if (!response.status.isSuccess()) {
            when (response.status.value) {
                in 400..499 -> {
                    val error = response.body<Error>()
                    throw OKAPIClientException(error)
                }

                else -> throw IllegalArgumentException("Unexpected response: ${response.status}")
            }
        }

        return response.body<Map<String, Geocache>>()
    }

    /**
     * Calls [caches/search/bbox](https://opencaching.pl/okapi/services/caches/search/bbox.html) endpoint
     */
    public suspend fun searchInBoundingBox(bbox: BoundingBox): List<Geocache> {
        val url = "$apiUrl/caches/search/bbox"
        val queryParams = mapOf("bbox" to bbox.toPipeFormat())

        val response = httpClient.get(url) {
            accept(Application.Json)
            queryParams.forEach { (key, value) -> parameter(key, value) }
            addAuth(url, queryParams)
        }

        return response.body<List<Geocache>>()
    }

    /**
     * Calls [caches/geocache](https://opencaching.pl/okapi/services/caches/geocache.html) endpoint.
     */
    public suspend fun getGeocache(code: String): Geocache {
        val url = "$apiUrl/caches/geocache"
        val queryParams = mapOf(
            "cache_code" to code,
            "fields" to Geocache.allParams,
            "owner_fields" to User.allParams,
        )

        val response = httpClient.get(url) {
            accept(Application.Json)
            queryParams.forEach { (key, value) -> parameter(key, value) }
            addAuth(url, queryParams)
        }

        if (!response.status.isSuccess()) {
            when (response.status.value) {
                in 400..499 -> {
                    val error = response.body<Error>()
                    throw OKAPIClientException(error)
                }

                else -> throw IllegalArgumentException("Unexpected response: ${response.status}")
            }
        }

        return response.body<Geocache>()
    }

    /**
     * Calls [logs/logs](https://opencaching.pl/okapi/services/logs/logs.html) endpoint.
     */
    public suspend fun getGeocacheLogs(code: String, limit: Int = 10, offset: Int = 10): List<Log> {
        val url = "$apiUrl/logs/logs"
        val queryParams = mapOf(
            "cache_code" to code,
            "fields" to Log.allParams,
            "user_fields" to User.allParams,
            "offset" to offset.toString(),
            "limit" to limit.toString(),
        )

        val response = httpClient.get(url) {
            accept(Application.Json)
            queryParams.forEach { (key, value) -> parameter(key, value) }
            addAuth(url, queryParams)
        }

        if (!response.status.isSuccess()) {
            when (response.status.value) {
                in 400..499 -> {
                    val error = response.body<Error>()
                    throw OKAPIClientException(error)
                }

                else -> throw IllegalArgumentException("Unexpected response: ${response.status}")
            }
        }

        return response.body<List<Log>>()
    }

    /**
     * Gets the currently authenticated user's information.
     *
     * This is a Level 3 method that requires authentication.
     *
     * Calls [users/user](https://opencaching.pl/okapi/services/users/user.html) endpoint.
     *
     * @throws IllegalStateException if the client is not authenticated
     */
    public suspend fun getCurrentUser(): User {
        if (!isAuthenticated) {
            throw IllegalStateException("This method requires authentication. Create client with accessToken.")
        }

        val url = "$apiUrl/users/user"
        val queryParams = mapOf("fields" to User.allParams)

        val response = httpClient.get(url) {
            accept(Application.Json)
            queryParams.forEach { (key, value) -> parameter(key, value) }
            addAuth(url, queryParams)
        }

        if (!response.status.isSuccess()) {
            when (response.status.value) {
                in 400..499 -> {
                    val error = response.body<Error>()
                    throw OKAPIClientException(error)
                }

                else -> throw IllegalArgumentException("Unexpected response: ${response.status}")
            }
        }

        return response.body<User>()
    }
}

/**
 * Thrown when the OKAPI server returns a 4xx status code.
 */
public class OKAPIClientException(
    public val error: Error,
) : IllegalArgumentException(error.error.developerMessage)

public fun main(): Unit = runBlocking {
    val consumerKey = "***"
    val consumerSecret = "***"
    val geocacheCode = "OP9655"
    val client = OpencachingClient(
        httpClient = HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
        },
        consumerCredentials = ConsumerCredentials(key = consumerKey, secret = consumerSecret),
    )
    println("Hello!")

    client.getGeocache(geocacheCode).let {
        println(it)
    }

    client.getGeocacheLogs(geocacheCode).reversed().forEach {
        println("${it.user.username} • ${it.date} • ${it.type}\n${it.comment}\n\n")
    }
}
