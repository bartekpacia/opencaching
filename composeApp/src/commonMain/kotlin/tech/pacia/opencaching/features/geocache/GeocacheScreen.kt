package tech.pacia.opencaching.features.geocache

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.opencaching.data.FullGeocache
import tech.pacia.opencaching.data.Geocache
import tech.pacia.opencaching.data.Location
import tech.pacia.opencaching.data.User
import tech.pacia.opencaching.ui.theme.OpencachingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeocacheScreen(
    geocache: FullGeocache,
    modifier: Modifier = Modifier,
    onNavUp: () -> Unit = {},
    onNavigateToDescription: () -> Unit = {},
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
                    text = "Placed by: ${geocache.owner.username}\non ${geocache.dateHidden}",
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
                    subtitle = geocache.description.split(" ").take(4).joinToString(" ") + "...",
                    onClick = onNavigateToDescription,
                )

                GeocacheInfoTile(
                    icon = Icons.Filled.MonitorHeart,
                    title = "Activity",
                    subtitle = "Found 2 days ago",
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
    OpencachingTheme(darkTheme = true) {
        GeocacheScreen(geocache = sampleGeocache) {}
    }
}

private val sampleGeocache = FullGeocache(
    code = "GC1234",
    name = "Drewniany most na długiej rzece Rudzie XXD",
    location = Location(latitude = 50.196168, longitude = 18.446953),
    type = Geocache.Type.Traditional,
    status = Geocache.Status.Available,
    difficulty = 2.5f,
    terrain = 2.5f,
    size = 2,
    owner = User(
        username = "Bartek_Wojak",
        uuid = "1234",
        profileUrl = "https://opencaching.pl/images/avatars/1234.jpg",
    ),
    description = "This is a test geocache",
    url = "https://opencaching.pl/viewcache.php?wp=OP9655",
    hint = "Hint: it's here",
    dateHidden = "12/12/2022",
    recommendations = 3,
)
