package tech.pacia.opencaching.features.geocache.description

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            SegmentedButton(
                                icon = {},
                                selected = viewMode == ViewMode.Web,
                                onClick = { viewMode = ViewMode.Web },
                                shape = MaterialTheme.shapes.small,
                            ) {
                                Text("Web")
                            }

                            SegmentedButton(
                                icon = {},
                                selected = viewMode == ViewMode.Text,
                                onClick = { viewMode = ViewMode.Text },
                                shape = MaterialTheme.shapes.small,
                            ) {
                                Text("Text")
                            }
                        }
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
            html = "xdxdxd",
        )
    }
}
