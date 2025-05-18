package tech.pacia.opencaching.features.map

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.okapi.client.models.BoundingBox
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.okapi.client.models.Location

@Composable
fun MapScreen(
    onNavigateToGeocache: (Geocache) -> Unit,
    onMapBoundsChange: (BoundingBox) -> Unit = {},
    isUpdating: Boolean = false,
    geocaches: Map<String, Geocache> = emptyMap(),
    modifier: Modifier = Modifier,
) {
    val centerOfRudy = remember { Location(latitude = 50.196168, longitude = 18.446953) }

    Map(
        modifier = modifier,
        center = centerOfRudy,
        caches = geocaches.entries.map { it.value },
        onGeocacheClick = { code ->
            val geocache = geocaches[code] ?: return@Map
            onNavigateToGeocache(geocache)
        },
        onMapBoundsChange = { boundingBox ->
            if (boundingBox == null) return@Map
            onMapBoundsChange(boundingBox)
        },
    )

    if (isUpdating) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen(onNavigateToGeocache = {})
}
