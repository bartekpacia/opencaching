package tech.pacia.opencaching.features.geocache.description

import androidx.compose.runtime.Composable

@Composable
fun GeocacheDescriptionRoute(
    html: String,
    onNavUp: () -> Unit,
) {
    GeocacheDescriptionScreen(
        html = html,
        onNavUp = onNavUp,
    )
}
