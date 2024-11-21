package tech.pacia.opencaching.features.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.okapi.client.models.Geocache

@Suppress("unused")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToGeocache: (Geocache) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Profile tab",
    )
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onNavigateToGeocache = {})
}
