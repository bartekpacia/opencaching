package tech.pacia.opencaching.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uuid: String,
    val username: String,
    @SerialName("profile_url") val profileUrl: String,
)
