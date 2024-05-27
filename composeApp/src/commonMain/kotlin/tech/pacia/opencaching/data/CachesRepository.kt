package tech.pacia.opencaching.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tech.pacia.opencaching.debugLog

const val API_URL = "https://opencaching.pl/okapi/services"
const val CONSUMER_KEY = "duM7DuHSXQtLK7PCx9ee"

class CachesRepository(private val client: HttpClient) {

    /// https://opencaching.pl/okapi/services/caches/shortcuts/search_and_retrieve.html
    suspend fun searchAndRetrieve(bbox: BoundingBox): Map<String, Geocache> {
        val response = client.get("$API_URL/caches/shortcuts/search_and_retrieve") {
            accept(ContentType.Application.Json)
            parameter("consumer_key", CONSUMER_KEY)
            parameter("search_method", "services/caches/search/bbox")
            parameter("search_params", Json.encodeToString(mapOf("bbox" to bbox.toPipeFormat())))
            parameter("retr_method", "services/caches/geocaches")
            parameter(
                "retr_params",
                Json.encodeToString(mapOf("fields" to "code|name|location|status|type|url|owner"))
            )
            parameter("wrap", false)
        }


        val body = response.body<Map<String, Geocache>>()

        debugLog("CachesRepository", "response: got ${body.values.size} geocaches")

        return body
    }

    suspend fun searchInBoundingBox(bbox: BoundingBox): List<Geocache> {
        val response = client.get("$API_URL/caches/search/bbox") {
            accept(ContentType.Application.Json)
            parameter("consumer_key", CONSUMER_KEY)
            parameter("bbox", bbox.toPipeFormat())
        }

        debugLog("CachesRepository", "response: $response")

        return response.body()
    }

    /// https://opencaching.pl/okapi/services/caches/geocache.html
    suspend fun getGeocache(code: String): Geocache {
        val response = client.get("$API_URL/caches/geocache") {
            accept(ContentType.Application.Json)
            parameter("consumer_key", CONSUMER_KEY)
            parameter("cache_code", code)
            parameter("fields", "code|name|location|status|type|url|owner")
        }

        debugLog("CachesRepository", "response: $response")

        return response.body()
    }
}