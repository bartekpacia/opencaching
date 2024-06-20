package tech.pacia.opencaching.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val defaultHttpClient = HttpClient {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            },
        )
    }
}

class UserRepository(@Suppress("UnusedPrivateProperty") private val client: HttpClient = defaultHttpClient) {

    @Suppress("EmptyFunctionBlock")
    suspend fun signIn(
        @Suppress("UNUSED_PARAMETER") username: String,
        @Suppress("UNUSED_PARAMETER")password: String,
    ) {
    }
}
