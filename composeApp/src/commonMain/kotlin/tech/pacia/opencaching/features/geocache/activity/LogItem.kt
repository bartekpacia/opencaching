package tech.pacia.opencaching.features.geocache.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import opencaching.composeapp.generated.resources.Res
import opencaching.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.okapi.client.models.Log

@Composable
fun LogItem(
    profileUrl: String,
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
        CoilImage(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp),
            imageModel = { profileUrl },
            previewPlaceholder = painterResource(Res.drawable.profile),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = username,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "$userFinds finds",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Row {
                Text(
                    text = buildString {
                        val status = when (type) {
                            Log.Type.DidFind -> "😁 Found it"
                            Log.Type.DidNotFind -> "😓 Didn't find it"
                            Log.Type.Comment -> "💬 Commented"
                            Log.Type.WillAttend -> "🙌 Will attend"
                            Log.Type.Attended -> "😆 Attended"

                            Log.Type.TemporarilyUnavailable -> "❌ Temporarily unavailable"
                            Log.Type.ReadyToSearch -> "✅ Ready to search"
                            Log.Type.Archived -> "🗑️ Archived"
                            Log.Type.Locked -> "🔒 Locked"

                            Log.Type.NeedsMaintenance -> "🔧 Needs maintenance"
                            Log.Type.Moved -> "➡️ Moved"
                            Log.Type.OCTeamComment -> "💬 Commented"
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
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(content, style = MaterialTheme.typography.bodySmall)
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
        content = """
        Found it! 🎉


        This cache was a fun challenge! After a bit of searching around, I finally spotted
        the clever hiding spot. The coordinates were pretty accurate, and the camouflage was
        really impressive — had me stumped for a moment! Loved the walk to the location, the
        views were awesome, and the weather was perfect for some geocaching.


        Took nothing, left nothing but signed the logbook. Thanks for the adventure!
        Looking forward to more caches in this area!
        """.trimIndent().replace(Regex("(\n*)\n"), "$1"),
        type = Log.Type.DidFind,
        date = Instant.parse("2022-12-12T11:21:37+00:00"),
    )
}
