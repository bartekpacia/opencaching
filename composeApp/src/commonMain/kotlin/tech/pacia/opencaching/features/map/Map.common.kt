package tech.pacia.opencaching.features.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.pacia.okapi.client.models.BoundingBox
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.okapi.client.models.Location

@Composable
expect fun Map(
    modifier: Modifier = Modifier,
    center: Location,
    caches: List<Geocache>,
    onGeocacheClick: (String) -> Unit,
    onMapBoundsChange: (BoundingBox?) -> Unit,
)
