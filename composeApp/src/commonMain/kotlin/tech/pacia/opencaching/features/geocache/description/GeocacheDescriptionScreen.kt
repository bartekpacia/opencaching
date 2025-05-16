package tech.pacia.opencaching.features.geocache.description

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.opencaching.common.WebView
import tech.pacia.opencaching.ui.theme.OpencachingTheme

private enum class ViewMode { Web, Text }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeocacheDescriptionScreen(
    html: String,
    modifier: Modifier = Modifier,
    onNavUp: () -> Unit = {},
) {
    var viewMode by remember { mutableStateOf(ViewMode.Web) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                title = {
                    SingleChoiceSegmentedButtonRow {
                        SegmentedButton(
                            selected = viewMode == ViewMode.Web,
                            onClick = { viewMode = ViewMode.Web },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            label = { Text("Web") },
                        )

                        SegmentedButton(
                            selected = viewMode == ViewMode.Text,
                            onClick = { viewMode = ViewMode.Text },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            label = { Text("Text") },
                        )
                    }
                },
            )
        },
    ) { padding ->
        WebView(
            modifier = Modifier.padding(padding),
            html = html,
        )
    }
}

@Preview
@Composable
private fun GeocacheDescriptionScreenPreview() {
    OpencachingTheme(darkThemeActive = true) {
        GeocacheDescriptionScreen(
            html = "<p>Some HTML content</p>",
        )
    }
}
