package tech.pacia.opencaching

import androidx.compose.runtime.Composable
import tech.pacia.opencaching.ui.theme.OpencachingTheme

@Composable
fun App() {
    OpencachingTheme {
        OpencachingNavHost()
    }
}
