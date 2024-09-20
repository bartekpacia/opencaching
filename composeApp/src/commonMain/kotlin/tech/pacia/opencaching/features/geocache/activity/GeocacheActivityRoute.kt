package tech.pacia.opencaching.features.geocache.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.pacia.okapi.client.models.Log
import tech.pacia.opencaching.data.CachesRepository

@Composable
fun GeocacheActivityRoute(
    code: String,
    onNavUp: () -> Unit,
) {
    val repository = remember { CachesRepository() }
    val logs = remember { mutableStateOf<List<Log>?>(null) }

    LaunchedEffect(code) {
        logs.value = repository.getLogs(code)
    }

    if (logs.value == null) {
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
        GeocacheActivityScreen(
            logs = logs.value!!,
            onNavUp = onNavUp,
        )
    }
}
