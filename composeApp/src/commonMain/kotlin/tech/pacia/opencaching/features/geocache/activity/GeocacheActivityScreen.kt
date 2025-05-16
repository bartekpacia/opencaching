package tech.pacia.opencaching.features.geocache.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.okapi.client.models.Log
import tech.pacia.okapi.client.models.User

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
                            selected = viewMode == ViewMode.All,
                            onClick = { viewMode = ViewMode.All },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            label = { Text("All") },
                        )

                        SegmentedButton(
                            selected = viewMode == ViewMode.Friends,
                            onClick = { viewMode = ViewMode.Friends },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            label = { Text("Friends") },
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(padding),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(logs) { index, item ->
                LogItem(
                    profileUrl = item.user.profileUrl,
                    username = item.user.username,
                    userFinds = item.user.cachesFound,
                    content = item.comment,
                    type = item.type,
                    date = item.date,
                )

                if (index < logs.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun GeocacheActivityScreenPreview() {
    GeocacheActivityScreen(
        logs = sampleLogs,
    )
}

private val sampleLogs = listOf(
    Log(
        uuid = "123e4567-e89b-12d3-a456-426614174000",
        cacheCode = "GC1A2BC",
        date = Instant.parse("2024-08-15T10:15:30Z"),
        user = SampleUsers.first,
        type = Log.Type.DidFind,
        ocTeamEntry = false,
        wasRecommended = true,
        comment = "Found it after a long hike! The view was worth it.",
        images = listOf(
            // Image("view.jpg", "A stunning view at the cache location")
        ),
        dateCreated = LocalDate.parse("2024-08-15"),
        lastModified = LocalDate.parse("2024-08-15"),
    ),
    Log(
        uuid = "234e4567-e89b-12d3-a456-426614174001",
        cacheCode = "GC2B3CD",
        date = Instant.parse("2024-09-05T09:00:00Z"),
        user = SampleUsers.second,
        type = Log.Type.DidNotFind,
        ocTeamEntry = false,
        wasRecommended = false,
        comment = "Searched for an hour but couldn't locate the cache. Might need maintenance.",
        images = emptyList(),
        dateCreated = LocalDate.parse("2024-09-05"),
        lastModified = LocalDate.parse("2024-09-05"),
    ),
    Log(
        uuid = "345e4567-e89b-12d3-a456-426614174002",
        cacheCode = "GC3C4DE",
        date = Instant.parse("2024-09-10T15:30:00Z"),
        user = SampleUsers.third,
        type = Log.Type.NeedsMaintenance,
        ocTeamEntry = false,
        wasRecommended = false,
        comment = "The container is damaged and the logbook is soaked.",
        images = listOf(
            // Image("damage.jpg", "The broken container")
        ),
        dateCreated = LocalDate.parse("2024-09-10"),
        lastModified = LocalDate.parse("2024-09-11"),
    ),
    Log(
        uuid = "456e4567-e89b-12d3-a456-426614174003",
        cacheCode = "GC4D5EF",
        date = Instant.parse("2024-09-12T11:00:00Z"),
        user = SampleUsers.fourth,
        type = Log.Type.OCTeamComment,
        ocTeamEntry = true,
        wasRecommended = false,
        comment = "Cache temporarily unavailable due to nearby construction work.",
        images = emptyList(),
        dateCreated = LocalDate.parse("2024-09-12"),
        lastModified = LocalDate.parse("2024-09-12"),
    ),
)

private object SampleUsers {
    val first = User(
        uuid = "u123e4567-e89b-12d3-a456-426614174000",
        username = "GeoHunter",
        profileUrl = "https://example.com/avatar1.png",
        cachesFound = 150,
        cachesHidden = 10,
    )

    val second = User(
        uuid = "u234e4567-e89b-12d3-a456-426614174001",
        username = "LostSeeker",
        profileUrl = "https://example.com/avatar2.png",
        cachesFound = 45,
        cachesHidden = 2,
    )

    val third = User(
        uuid = "u345e4567-e89b-12d3-a456-426614174002",
        username = "CacheMaster",
        profileUrl = "https://example.com/avatar3.png",
        cachesFound = 300,
        cachesHidden = 25,
    )

    val fourth = User(
        uuid = "u456e4567-e89b-12d3-a456-426614174003",
        username = "TeamOC",
        profileUrl = "https://example.com/avatar4.png",
        cachesFound = 0,
        cachesHidden = 0,
    )
}
