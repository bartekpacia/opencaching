package tech.pacia.okapi.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
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

public class OpencachingClient public constructor(
    private val httpClient: HttpClient = defaultHttpClient,
    private val consumerKey: String,
    public val apiUrl: String = OKAPI_URL_PL,
) {
    /**
     * Calls [caches/shortcuts/search_and_retrieve](https://opencaching.pl/okapi/services/caches/shortcuts/search_and_retrieve.html) endpoint.
     */
    public suspend fun searchAndRetrieve(bbox: BoundingBox): Map<String, Geocache> {
        val response = httpClient.get("$apiUrl/caches/shortcuts/search_and_retrieve") {
            accept(Application.Json)
            parameter("consumer_key", consumerKey)
            parameter("search_method", "services/caches/search/bbox")
            parameter("search_params", Json.encodeToString(mapOf("bbox" to bbox.toPipeFormat())))
            parameter("retr_method", "services/caches/geocaches")
            parameter(
                key = "retr_params",
                value = Json.encodeToString(
                    mapOf(
                        "fields" to Geocache.allParams,
                        "owner_fields" to User.allParams,
                    ),
                ),
            )
            parameter("wrap", false)
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
        val response = httpClient.get("$apiUrl/caches/search/bbox") {
            accept(Application.Json)
            parameter("consumer_key", consumerKey)
            parameter("bbox", bbox.toPipeFormat())
        }

        return response.body<List<Geocache>>()
    }

    /**
     * Calls [caches/geocache](https://opencaching.pl/okapi/services/caches/geocache.html) endpoint.
     */
    public suspend fun getGeocache(code: String): Geocache {
        val response = httpClient.get("$apiUrl/caches/geocache") {
            accept(Application.Json)
            parameter("consumer_key", consumerKey)
            parameter("cache_code", code)
            parameter("fields", Geocache.allParams)
            parameter("owner_fields", User.allParams)
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
        val response = httpClient.get("$apiUrl/logs/logs") {
            accept(Application.Json)
            parameter("consumer_key", consumerKey)
            parameter("cache_code", code)
            parameter("fields", Log.allParams)
            parameter("user_fields", User.allParams)
            parameter("offset", offset)
            parameter("limit", limit)
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
}

/**
 * Thrown when the OKAPI server returns a 4xx status code.
 */
public class OKAPIClientException(
    public val error: Error,
) : IllegalArgumentException(error.error.developerMessage)

public fun main(): Unit = runBlocking {
    val consumerKey = "***"
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
        consumerKey = consumerKey,
    )
    println("Hello!")

    client.getGeocache(geocacheCode).let {
        println(it)
    }

    client.getGeocacheLogs(geocacheCode).reversed().forEach {
        println("${it.user.username} • ${it.date} • ${it.type}\n${it.comment}\n\n")
    }
}
