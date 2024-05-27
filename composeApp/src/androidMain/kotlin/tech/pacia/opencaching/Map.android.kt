package tech.pacia.opencaching

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import tech.pacia.opencaching.data.BoundingBox
import tech.pacia.opencaching.data.Geocache
import tech.pacia.opencaching.data.Location
import tech.pacia.opencaching.navigation.GeocachePage

@Composable
actual fun Map(
    modifier: Modifier,
    center: Location,
    caches: List<Geocache>,
    onMapBoundsChange: (BoundingBox?) -> Unit,
) {
    debugLog("Map", "recomposition!")

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center.toLatLng(), 14f)
    }

//    LaunchedEffect(cameraPositionState.position.target) {
//        debugLog("Map", "moving!")
//        onMapBoundsChange(cameraPositionState.projection?.visibleRegion?.latLngBounds?.toBoundingBox())
//    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        googleMapOptionsFactory = {
            GoogleMapOptions().mapType(MapType.SATELLITE.value)
        },
        onMapLoaded = {
            onMapBoundsChange(
                cameraPositionState.projection?.visibleRegion?.latLngBounds?.toBoundingBox()
            )
        }
    ) {
        for (cache in caches) {
            val navStack = LocalNavigationStack.current

            Marker(
                state = rememberMarkerState(key = cache.code, position = cache.location.toLatLng()),
                title = cache.name,
                snippet = cache.code,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                onInfoWindowClick = {
                    navStack.push(GeocachePage(cache.code))
                }
            )
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