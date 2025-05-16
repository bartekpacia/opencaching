package tech.pacia.opencaching.common

import android.graphics.Color
import android.widget.TextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
actual fun WebView(
    modifier: Modifier,
    html: String,
) {
    val textColor = if (isSystemInDarkTheme()) Color.WHITE else Color.BLACK
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                setTextIsSelectable(true)
            }
        },
        update = {
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
            it.setTextColor(textColor)
        },
    )
}
