package tech.pacia.opencaching.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uuid: String,
    val username: String,
    val profile_url: String
)