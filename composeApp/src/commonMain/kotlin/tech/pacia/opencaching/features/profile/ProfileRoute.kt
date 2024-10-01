package tech.pacia.opencaching.features.profile


import androidx.compose.runtime.Composable
import tech.pacia.okapi.client.models.Geocache

@Composable
fun ProfileRoute(
    onNavigateToGeocache: (Geocache) -> Unit,
) {
    ProfileScreen(
        onNavigateToGeocache = onNavigateToGeocache,
    )
}
