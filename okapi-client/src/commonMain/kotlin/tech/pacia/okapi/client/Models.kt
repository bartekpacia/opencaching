package tech.pacia.okapi.client

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO: Use kotlinx.date type instead of String for string dates in ISO 8601

@Serializable
public data class Geocache(
    val code: String,
    val name: String,
    val location: Location,
    val status: Status,
    val type: Type,
) {
    public enum class Type { Traditional, Multi, Moving, Quiz, Own, Webcam, Other }

    public enum class Status { Available, TEMPORARILY_UNAVAILABLE, Archived }
}

@Serializable
public data class FullGeocache(
    val code: String,
    val name: String,
    val location: Location,
    val status: Geocache.Status,
    val type: Geocache.Type,
    val url: String,
    val owner: User,
    val description: String,
    val difficulty: Float,
    val terrain: Float,
    val size: Int,
    val hint: String,
    @SerialName("date_hidden") val dateHidden: String,
    val recommendations: Int,
)

@Serializable(with = LocationAsStringSerializer::class)
public data class Location(
    val latitude: Double,
    val longitude: Double,
)

public data class BoundingBox(
    val north: Double,
    val east: Double,
    val south: Double,
    val west: Double,
) {
    public fun toPipeFormat(): String = "$south|$west|$north|$east"
}

private object LocationAsStringSerializer : KSerializer<Location> {
    override val descriptor = PrimitiveSerialDescriptor("location", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Location) {
        val string = "${value.latitude}|${value.longitude}"
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): Location {
        val string = decoder.decodeString()
        val latlng = string.split('|')

        val lat = latlng[0].toDouble()
        val lng = latlng[1].toDouble()

        return Location(lat, lng)
    }
}

/**
 * Structure is documented in [the docs](https://opencaching.pl/okapi/services/users/user.html)
 */
@Serializable
public data class User(
    @SerialName("uuid") val uuid: String,
    @SerialName("username") val username: String,
    @SerialName("profile_url") val profileUrl: String,
)


/**
 * Structure is documented in [the docs](https://opencaching.pl/okapi/services/logs/entry.html)
 */
@Serializable
public data class Log(
    @SerialName("uuid") val uuid: String,
    @SerialName("cache_code") val cacheCode: String,
    @SerialName("date") val dateHidden: String,
    @SerialName("user") val user: User,
    @SerialName("type") val type: Type,
    @SerialName("oc_team_entry") val ocTeamEntry: Boolean,
    @SerialName("was_recommended") val wasRecommended: Boolean,
    // @SerialName("needs_maintenance2") val needsMaintenance: Boolean?,
    @SerialName("comment") val comment: String,
    @SerialName("images") val images: List<Image>,
    @SerialName("date_created") val dateCreated: String,
    @SerialName("last_modified") val lastModified: String,
) {
    @Serializable(with = LogTypeSerializer::class)
    public enum class Type(internal val id: String) {

        // Primary types, commonly used by all Opencaching installations:
        DidFind("Found it"),
        DidNotFind("Didn't find it"),
        Comment("Comment"),
        WillAttend("Will attend"),
        Attended("Attended"),

        // Types which indicate a change of state of the geocache or confirm the state at the given time
        TemporarilyUnavailable("Temporarily unavailable"),
        ReadyToSearch("Ready to search"),
        Archived("Archived"),
        Locked("Locked"),

        // Other types
        NeedsMaintenance("Needs maintenance"),
        Moved("Moved"),
        OCTeamComment("OC Team comment"),
    }

    @Serializable
    public data class Image(
        @SerialName("uuid") val uuid: String,
        @SerialName("url") val url: String,
        @SerialName("thumb_url") val thumbUrl: String,
        @SerialName("caption") val caption: String,
        @SerialName("is_spoiler") val spoiler: Boolean,
    )
}

private object LogTypeSerializer : KSerializer<Log.Type> {
    override val descriptor = PrimitiveSerialDescriptor("type", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Log.Type) {
        encoder.encodeString(value.id)
    }

    override fun deserialize(decoder: Decoder): Log.Type {
        val id = decoder.decodeString()
        return when (id) {
            Log.Type.DidFind.id -> Log.Type.DidFind
            Log.Type.DidNotFind.id -> Log.Type.DidNotFind
            Log.Type.Comment.id -> Log.Type.Comment
            Log.Type.WillAttend.id -> Log.Type.WillAttend
            Log.Type.Attended.id -> Log.Type.Attended
            Log.Type.TemporarilyUnavailable.id -> Log.Type.TemporarilyUnavailable
            Log.Type.ReadyToSearch.id -> Log.Type.ReadyToSearch
            Log.Type.Archived.id -> Log.Type.Archived
            Log.Type.Locked.id -> Log.Type.Locked
            Log.Type.NeedsMaintenance.id -> Log.Type.NeedsMaintenance
            Log.Type.Moved.id -> Log.Type.Moved
            Log.Type.OCTeamComment.id -> Log.Type.OCTeamComment
            else -> Log.Type.Comment
        }
    }
}
