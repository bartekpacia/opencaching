package tech.pacia.opencaching.features.map

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.pacia.okapi.client.models.BoundingBox
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.okapi.client.models.Location

@Composable
actual fun Map(
    modifier: Modifier,
    center: Location,
    caches: List<Geocache>,
    onGeocacheClick: (String) -> Unit,
    onMapBoundsChange: (BoundingBox?) -> Unit,
) {
    Text("Desktop Map stub")
}
