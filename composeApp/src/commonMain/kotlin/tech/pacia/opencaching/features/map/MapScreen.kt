package tech.pacia.opencaching.features.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.opencaching.data.CachesRepository
import tech.pacia.opencaching.data.Geocache
import tech.pacia.opencaching.data.Location
import tech.pacia.opencaching.debugLog
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
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

    val repository = remember { CachesRepository() }

    var lastInstant by remember { mutableStateOf(Clock.System.now()) }

    var selectedNavBarItem by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Map") },
            )
        },
        content = { padding ->
            Map(
                modifier = Modifier.padding(padding),
                center = centerOfRudy,
                caches = geocaches.entries.map { it.value },
                onGeocacheClick = { code -> onNavigateToGeocache(geocaches[code]!!) },
                onMapBoundsChange = {
                    if (it == null) return@Map

                    val currentInstant = Clock.System.now()
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
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = 0 == selectedNavBarItem,
                    onClick = { selectedNavBarItem = 0 },
                    icon = { Icon(Icons.Rounded.Map, contentDescription = "Map") },
                    label = { Text("Map") },
                )

                NavigationBarItem(
                    selected = 1 == selectedNavBarItem,
                    onClick = { selectedNavBarItem = 1 },
                    icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                )
            }
        },
    )
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen(onNavigateToGeocache = {})
}
