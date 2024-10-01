package tech.pacia.opencaching.features.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.okapi.client.models.Geocache

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToGeocache: (Geocache) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("Profile tab")
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onNavigateToGeocache = {})
}
