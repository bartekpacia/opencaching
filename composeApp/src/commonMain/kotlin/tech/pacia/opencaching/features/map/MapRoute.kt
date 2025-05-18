package tech.pacia.opencaching.features.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.opencaching.data.CachesRepository

@Composable
fun MapRoute(
    onNavigateToGeocache: (Geocache) -> Unit,
) {
    val repository = remember { CachesRepository() }
    val mapViewModel = viewModel { MapViewModel(cachesRepository = repository) }
    val geocaches = mapViewModel.geocaches.collectAsState().value
    val isUpdating = mapViewModel.isUpdating.collectAsState().value

    MapScreen(
        onNavigateToGeocache = onNavigateToGeocache,
        geocaches = geocaches,
        isUpdating = isUpdating,
        onMapBoundsChange = { boundingBox ->
            mapViewModel.fetch(boundingBox)
        },
    )
}
