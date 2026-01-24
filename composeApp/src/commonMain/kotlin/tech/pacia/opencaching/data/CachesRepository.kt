package tech.pacia.opencaching.data

import opencaching.composeApp.BuildConfig
import tech.pacia.okapi.client.OpencachingClient
import tech.pacia.okapi.client.models.BoundingBox
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.okapi.client.models.Log

private const val API_URL = "https://opencaching.pl/okapi/services"
private const val CONSUMER_KEY = BuildConfig.CONSUMER_KEY
private const val CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET

private val defaultOpencachingClient = OpencachingClient(
    consumerKey = CONSUMER_KEY,
    apiUrl = API_URL,
)

// I plan to make this repo to be offline-first.
//  See https://developer.android.com/topic/architecture/data-layer/offline-first

class CachesRepository(private val client: OpencachingClient = defaultOpencachingClient) {

    suspend fun searchAndRetrieve(bbox: BoundingBox): Map<String, Geocache> {
        return client.searchAndRetrieve(bbox)
    }

    suspend fun searchInBoundingBox(bbox: BoundingBox): List<Geocache> {
        return client.searchInBoundingBox(bbox)
    }

    suspend fun getGeocache(code: String): Geocache = client.getGeocache(code)

    suspend fun getLogs(code: String, limit: Int = 10, offset: Int = 0): List<Log> {
        return client.getGeocacheLogs(code, limit = limit, offset = offset)
    }
}
