package tech.pacia.opencaching.features.geocache.description

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.pacia.opencaching.data.CachesRepository
import tech.pacia.opencaching.data.FullGeocache

@Composable
fun GeocacheDescriptionRoute(
    code: String,
    onNavUp: () -> Unit,
) {
    val repository = remember { CachesRepository() }
    val geocache = remember { mutableStateOf<FullGeocache?>(null) }

    LaunchedEffect(code) {
        geocache.value = repository.getGeocache(code)
    }

    if (geocache.value == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
            )
        }
    } else {
        GeocacheDescriptionScreen(
            html = geocache.value!!.description,
            onNavUp = onNavUp,
        )
    }
}
