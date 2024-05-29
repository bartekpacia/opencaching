package tech.pacia.opencaching.features.geocache

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Rating(
    title: String,
    @FloatRange(from = 1.0, to = 5.0) rating: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = rating.toString(),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.labelMedium,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (i in 1..5) {
                val halved = i == rating.toInt() + 1 && rating % 1 != 0f

                val activeColor = MaterialTheme.colorScheme.primary
                val inactiveColor = MaterialTheme.colorScheme.inversePrimary

                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(12.dp)
                        .clip(RoundedCornerShape(3f.dp))
                        .drawBehind {
                            drawRect(
                                color = if (i - 0.5f <= rating) activeColor else inactiveColor,
                                size = if (!halved) size else size.copy(width = size.width / 2),
                            )

                            if (halved) {
                                drawRect(
                                    color = inactiveColor,
                                    topLeft = Offset(size.width / 2, 0f),
                                    size = size.copy(width = size.width / 2),
                                )
                            }
                        },
                )
            }
        }

        Text(
            text = title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun RatingPreview_1() {
    Rating(
        rating = 2f,
        title = "Difficulty",
    )
}

@Preview
@Composable
private fun RatingPreview_2() {
    Rating(
        rating = 2.5f,
        title = "Terrain",
    )
}
