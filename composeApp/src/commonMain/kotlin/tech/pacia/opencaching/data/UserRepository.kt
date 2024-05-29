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

class UserRepository(private val client: HttpClient = defaultHttpClient) {

    suspend fun signIn(username: String, password: String) {

    }
}
