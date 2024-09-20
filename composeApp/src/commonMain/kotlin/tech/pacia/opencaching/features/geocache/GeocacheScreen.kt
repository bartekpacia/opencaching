package tech.pacia.opencaching.features.geocache

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.okapi.client.models.Geocache
import tech.pacia.okapi.client.models.Location
import tech.pacia.okapi.client.models.User
import tech.pacia.opencaching.ui.theme.OpencachingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeocacheScreen(
    geocache: Geocache,
    modifier: Modifier = Modifier,
    onNavUp: () -> Unit = {},
    onNavigateToDescription: () -> Unit = {},
    onNavigateToActivity: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Geocache ${geocache.code}") },
                navigationIcon = {
                    IconButton(onClick = onNavUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = geocache.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                ) {
                    Text(geocache.code)
                    Text("•")
                    Text("${geocache.type}")
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Navigation,
                            contentDescription = "Navigate",
                        )

                        Text("Navigate")
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text("Log cache")
                    }
                }

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Heart icon",
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("${geocache.recommendations} recommendations")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Rating(rating = geocache.difficulty, title = "Difficulty")
                Rating(rating = geocache.terrain, title = "Terrain")
                Rating(rating = geocache.size.toFloat(), title = "Size")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Placed by: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(geocache.owner.username)
                        }

                        append("\non ${geocache.dateHidden}")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Gray),
                )

                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                ) {
                    TextButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.extraSmall,
                    ) {
                        Text("Hint")
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(Color.Gray),
                    )

                    TextButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.extraSmall,
                    ) {
                        Text("Message")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                GeocacheInfoTile(
                    icon = Icons.AutoMirrored.Rounded.List,
                    title = "Description",
                    subtitle =
                        geocache.shortDescription.ifBlank {
                            geocache.description.split(" ").take(4).joinToString(" ") + "..."
                        },
                    onClick = onNavigateToDescription,
                )

                GeocacheInfoTile(
                    icon = Icons.Filled.MonitorHeart,
                    title = "Activity",
                    subtitle = "Found 2 days ago",
                    onClick = onNavigateToActivity,
                )

                GeocacheInfoTile(
                    icon = Icons.Outlined.Info,
                    title = "Attributes",
                    subtitle = "Recommended for kids & 3 more",
                )
            }
        }
    }
}

@Composable
fun GeocacheInfoTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ListItem(
        modifier = modifier.clickable { onClick() },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
            )
        },
        headlineContent = { Text(title) },
        supportingContent = {
            Column {
                Text(subtitle)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1f.dp)
                        .background(Color.Gray),
                )
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "Select $title",
            )
        },
    )
}

@Preview
@Composable
private fun GeocacheScreemPreview() {
    OpencachingTheme {
        GeocacheScreen(geocache = sampleGeocache) {}
    }
}

@Preview
@Composable
private fun GeocacheScreemPreviewDark() {
    OpencachingTheme(darkThemeActive = true) {
        GeocacheScreen(geocache = sampleGeocache) {}
    }
}

private val sampleGeocache = Geocache(
    code = "OP9655",
    name = "Drewniany most na długiej rzece Rudzie XXD",
    location = Location(latitude = 50.196168, longitude = 18.446953),
    type = Geocache.Type.Traditional,
    status = Geocache.Status.Available,
    difficulty = 2.5f,
    terrain = 2.5f,
    size = Geocache.Size.Micro,
    owner = User(
        username = "Bartek_Wojak",
        uuid = "1234",
        profileUrl = "https://opencaching.pl/images/avatars/1234.jpg",
        cachesFound = 268,
        cachesHidden = 7,
    ),
    description = "This is a longer description. It's longer than the short description.",
    descriptions = mapOf(),
    url = "https://opencaching.pl/viewcache.php?wp=OP9655",
    hint2 = "Hint: it's here",
    recommendations = 3,
    needsMaintenance = false,
    gcCode = null,
    founds = 21,
    notFounds = 37,
    willattends = 0,
    watchers = 0,
    oxSize = 2.0f,
    tripTimeHours = null,
    tripDistanceKm = null,
    rating = 5.0f,
    shortDescription = "Some short descriptio",
    hints2 = mapOf(),
    attributeCodes = listOf(),
    atrributeNames = listOf(),
    trackablesCount = 0,
    trackables = listOf(),
    lastFound = null,
    dateHidden = Instant.parse("2022-12-12T11:21:37+00:00"),
    lastModified = Instant.parse("2022-12-12T11:22:37+00:00"),
    dateCreated = Instant.parse("2022-12-12T11:21:37+00:00"),
)
