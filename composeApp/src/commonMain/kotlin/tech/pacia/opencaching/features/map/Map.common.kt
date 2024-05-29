package tech.pacia.opencaching.features.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.pacia.opencaching.data.BoundingBox
import tech.pacia.opencaching.data.Geocache
import tech.pacia.opencaching.data.Location

@Composable
expect fun Map(
    modifier: Modifier = Modifier,
    center: Location,
    caches: List<Geocache>,
    onMapBoundsChange: (BoundingBox?) -> Unit,
)
