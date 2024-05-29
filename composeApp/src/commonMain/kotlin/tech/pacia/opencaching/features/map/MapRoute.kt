package tech.pacia.opencaching.features.map

import androidx.compose.runtime.Composable
import tech.pacia.opencaching.data.Geocache

@Composable
fun MapRoute(
    onNavigateToGeocache: (Geocache) -> Unit,
) {
    MapScreen(
        onNavigateToGeocache = onNavigateToGeocache,
    )
}
