package tech.pacia.opencaching.features.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tech.pacia.opencaching.data.BoundingBox
import tech.pacia.opencaching.data.Geocache
import tech.pacia.opencaching.data.Location

@OptIn(DelicateCoroutinesApi::class)
@Composable
actual fun Map(
    modifier: Modifier,
    center: Location,
    caches: List<Geocache>,
    onGeocacheClick: (String) -> Unit,
    onMapBoundsChange: (BoundingBox?) -> Unit
) {
    Log.d("Map", "recomposition!")

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center.toLatLng(), 14f)
    }

    LaunchedEffect(cameraPositionState.position.target) {
        onMapBoundsChange(cameraPositionState.projection?.visibleRegion?.latLngBounds?.toBoundingBox())
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        googleMapOptionsFactory = { GoogleMapOptions().mapType(MapType.SATELLITE.value) },
        onMapLoaded = {
            Log.d("Map", "loaded!")
            onMapBoundsChange(
                cameraPositionState.projection?.visibleRegion?.latLngBounds?.toBoundingBox(),
            )
        },
    ) {
        for (cache in caches) {
            key(cache.code) {
                Marker(
                    state = rememberMarkerState(key = cache.code, position = cache.location.toLatLng()),
                    title = cache.name,
                    snippet = cache.code,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                    onInfoWindowClick = {
                        // Deadlock: https://stackoverflow.com/questions/70279262/navigating-with-compose-not-working-with-google-maps-on-android
                        GlobalScope.launch(Dispatchers.Main) {
                            onGeocacheClick(cache.code)
                        }
                    },
                )
            }
        }
    }
}

fun Location.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun LatLngBounds.toBoundingBox(): BoundingBox {
    return BoundingBox(
        north = northeast.latitude,
        east = northeast.longitude,
        south = southwest.latitude,
        west = southwest.longitude,
    )
}
