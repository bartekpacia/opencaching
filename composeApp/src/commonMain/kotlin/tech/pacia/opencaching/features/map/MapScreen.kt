package tech.pacia.opencaching.features.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.okapi.client.models.Location
import tech.pacia.opencaching.data.CachesRepository
import tech.pacia.opencaching.debugLog
import kotlin.time.Clock.System
import kotlin.time.Duration.Companion.seconds

@Composable
fun MapScreen(
    onNavigateToGeocache: (Geocache) -> Unit,
    modifier: Modifier = Modifier,
) {
    val centerOfRudy = remember { Location(latitude = 50.196168, longitude = 18.446953) }

    val scope = rememberCoroutineScope()
    val geocaches = rememberSaveable(
        saver = mapSaver(
            save = { it.entries.associate { entry -> entry.key to Json.encodeToString(entry.value) } },
            restore = {
                mutableStateMapOf<String, Geocache>().apply {
                    putAll(
                        it.entries.map { entry ->
                            entry.key to Json.decodeFromString<Geocache>(entry.value as String)
                        },
                    )
                }
            },
        ),
        init = { mutableStateMapOf() },
    )

    // TODO: OpencachingClient instance should be injected somehow (try out Metro?)
    val repository = remember { CachesRepository() }

    var lastInstant by remember { mutableStateOf(System.now()) }

    Map(
        modifier = modifier,
        center = centerOfRudy,
        caches = geocaches.entries.map { it.value },
        onGeocacheClick = { code -> onNavigateToGeocache(geocaches[code]!!) },
        onMapBoundsChange = {
            if (it == null) return@Map

            val currentInstant = System.now()
            val duration = currentInstant - lastInstant
            lastInstant = currentInstant

            if (duration < 1.seconds) {
                return@Map
            }

            debugLog("MapScreen", "onMapBoundsChange: $it")

            scope.launch {
                delay(500)
                geocaches.putAll(repository.searchAndRetrieve(it))
            }
        },
    )
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen(onNavigateToGeocache = {})
}
