package tech.pacia.opencaching.features.geocache.description

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SlidingChooser(
    modifier: Modifier = Modifier,
    selectedItem: String,
    items: List<String>,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().background(Color.Cyan),
    ) {
        val selectedItemIndex = items.indexOf(selectedItem)
        val delta = maxWidth.value / 1
        
        val offset by animateIntOffsetAsState(
            targetValue = IntOffset((selectedItemIndex * delta).toInt(), 0),
            label = "offset"
        )
        
        Box(
            modifier = Modifier
                .width(delta.dp)
                .height(30.dp)
                .offset { offset }
                .background(Color.DarkGray)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            for (item in items) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = item,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SlidingChooserPreview_1() {
    SlidingChooser(
        items = listOf("Web", "Text"),
        selectedItem = "Text",
    )
}