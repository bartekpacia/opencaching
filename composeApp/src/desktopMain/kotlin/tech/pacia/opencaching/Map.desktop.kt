package tech.pacia.opencaching

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.pacia.opencaching.data.BoundingBox
import tech.pacia.opencaching.data.Geocache
import tech.pacia.opencaching.data.Location

@Composable
actual fun Map(
    modifier: Modifier,
    center: Location,
    caches: List<Geocache>,
    onMapBoundsChange: (BoundingBox?) -> Unit,
) {
    Text("Desktop Map stub")
}