package tech.pacia.opencaching.features.geocache.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.okapi.client.models.Log

private enum class ViewMode { All, Friends }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeocacheActivityScreen(
    logs: List<Log>,
    modifier: Modifier = Modifier,
    onNavUp: () -> Unit = {},
) {
    var viewMode by remember { mutableStateOf(ViewMode.All) }

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
                                selected = viewMode == ViewMode.All,
                                onClick = { viewMode = ViewMode.All },
                                shape = MaterialTheme.shapes.small,
                            ) {
                                Text("Web")
                            }

                            SegmentedButton(
                                icon = {},
                                selected = viewMode == ViewMode.Friends,
                                onClick = { viewMode = ViewMode.Friends },
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
        ) {
            itemsIndexed(logs) { _, item ->
                LogItem(
                    profileUrl = "TODO",
                    username = item.user.username,
                    userFinds = item.user.cachesFound,
                    content = item.comment,
                    type = item.type,
                    date = item.date,
                )
            }
        }
    }
}

@Composable
fun LogItem(
    @Suppress("UNUSED_PARAMETER") profileUrl: String,
    username: String,
    userFinds: Int,
    content: String,
    type: Log.Type,
    date: Instant,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .background(Color.Green),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                Text(username)
                Spacer(modifier = Modifier.width(8.dp))
                Text("$userFinds finds")
            }

            Row {
                Text(
                    buildString {
                        val status = when (type) {
                            Log.Type.DidFind -> "😁 Found it"
                            Log.Type.DidNotFind -> "😓 Didn't find it"
                            else -> ""
                        }

                        append(status)
                        append(" on ")

                        val dateFormat = LocalDateTime.Format {
                            dayOfMonth()
                            char('/')
                            monthNumber()
                            char('/')
                            year()
                        }

                        val localDateTime = date.toLocalDateTime(TimeZone.currentSystemDefault())
                        append(localDateTime.format(dateFormat))
                    },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(content)
        }
    }
}

@Preview
@Composable
fun LogItemPreview() {
    LogItem(
        profileUrl = "https://img.geocaching.com/{0}/5f3896f4-200d-41e0-b652-8f4d9af15722.jpg",
        username = "Bartek_Wojak",
        userFinds = 268,
        content = "Nice cache! TFTC!",
        type = Log.Type.DidFind,
        date = Instant.parse("2022-12-12T11:21:37+00:00"),
    )
}
