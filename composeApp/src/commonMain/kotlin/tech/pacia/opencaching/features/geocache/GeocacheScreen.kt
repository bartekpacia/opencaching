package tech.pacia.opencaching.features.geocache

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.opencaching.LocalNavigationStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeocacheScreen(code: String) {
    val navStack = LocalNavigationStack.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Geocache $code") },
                navigationIcon = {
                    IconButton(onClick = { navStack.pop() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },

        ) {
        Column {
            Text("Geocache details!")
        }
    }
}

@Preview
@Composable
fun SomeView() {
    Surface {
        Column {
            Text("XDXD")
        }
    }
}