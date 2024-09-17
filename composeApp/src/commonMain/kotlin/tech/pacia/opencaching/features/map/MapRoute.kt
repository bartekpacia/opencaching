package tech.pacia.opencaching.features.map

import androidx.compose.runtime.Composable
import tech.pacia.okapi.client.models.Geocache

@Composable
fun MapRoute(
    onNavigateToGeocache: (Geocache) -> Unit,
) {
    MapScreen(
        onNavigateToGeocache = onNavigateToGeocache,
    )
}
