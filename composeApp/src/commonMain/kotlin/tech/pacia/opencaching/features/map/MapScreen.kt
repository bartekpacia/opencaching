package tech.pacia.opencaching.features.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import tech.pacia.opencaching.LocalNavigationStack
import tech.pacia.opencaching.Map
import tech.pacia.opencaching.data.CachesRepository
import tech.pacia.opencaching.data.Geocache
import tech.pacia.opencaching.data.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val centerOfRudy = Location(latitude = 50.196168, longitude = 18.446953)

    val scope = rememberCoroutineScope()
    val geocaches = remember { mutableStateListOf<Geocache>() }

    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    val repository = remember { CachesRepository(httpClient) }

    val navStack = LocalNavigationStack.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map") },
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
            Map(
                Modifier.padding(8.dp),
                center = centerOfRudy,
                caches = geocaches.toList(),
                onMapBoundsChange = {
                    if (it == null) return@Map

                    scope.launch {
                        delay(500)
                        try {
                            geocaches.clear()
                            geocaches.addAll(repository.searchAndRetrieve(it).values)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            )

            Box(Modifier.height(50.dp).width(200.dp).background(Color.Red))
        }
    }
}
