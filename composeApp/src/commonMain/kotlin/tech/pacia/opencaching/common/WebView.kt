package tech.pacia.opencaching.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebView(
    modifier: Modifier,
    html: String,
)
