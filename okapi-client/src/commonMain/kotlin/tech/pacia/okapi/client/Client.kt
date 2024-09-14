package tech.pacia.okapi.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

public class Library {
    public fun someLibraryMethod(): Boolean {
        return true
    }
}

public const val OKAPI_URL_PL: String = "https://opencaching.pl/okapi/services"

public class OpencachingClient public constructor(
    private val httpClient: HttpClient,
    private val consumerKey: String,
    public val apiUrl: String = OKAPI_URL_PL,
) {
    private val fullParams =
        "code|name|location|status|type|url|owner|description|difficulty|terrain|size|hint|date_hidden|recommendations"

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
            parameter("retr_params", Json.encodeToString(mapOf("fields" to fullParams)))
            parameter("wrap", false)
        }

        // debugLog("CachesRepository", "response: ${response.bodyAsText()}")

        val body = response.body<Map<String, Geocache>>()

        // debugLog("CachesRepository", "response: got ${body.values.size} geocaches")

        return body
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

        // debugLog("CachesRepository", "response: $response")

        return response.body()
    }

    /**
     * Calls [caches/geocache](https://opencaching.pl/okapi/services/caches/geocache.html) endpoint.
     */
    public suspend fun getGeocache(code: String): FullGeocache {
        val response = httpClient.get("$apiUrl/caches/geocache") {
            accept(Application.Json)
            parameter("consumer_key", consumerKey)
            parameter("cache_code", code)
            parameter("fields", fullParams)
        }

        // debugLog("CachesRepository", "response: $response")

        return response.body()
    }

    /**
     * Calls [logs/logs](https://opencaching.pl/okapi/services/logs/logs.html) endpoint.
     */
    public suspend fun getGeocacheLogs(code: String): List<Log> {
        val response = httpClient.get("$apiUrl/logs/logs") {
            accept(Application.Json)
            parameter("consumer_key", consumerKey)
            parameter("cache_code", code)
            parameter(
                "fields",
                "uuid|cache_code|date|user|type|oc_team_entry|was_recommended|needs_maintenance2|comment|images|date_created|last_modified",
            )
            parameter("user_fields", "uuid|username|profile_url")
        }

        println(response.bodyAsText())

        return response.body()
    }
}

public fun main(): Unit = runBlocking {
    val consumerKey = "***"
    val geocacheCode = "OP9655"
    val client = OpencachingClient(
        httpClient = HttpClient() {
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

//    client.getGeocache(geocacheCode).let {
//        println(it)
//    }

     client.getGeocacheLogs(geocacheCode).reversed().forEach {
         println("${it.user.username} • ${it.dateCreated} • ${it.type}\n${it.comment}\n\n")
     }
}
