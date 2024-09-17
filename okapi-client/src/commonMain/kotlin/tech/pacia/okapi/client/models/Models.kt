package tech.pacia.okapi.client.models

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO: Use kotlinx.date type instead of String for string dates in ISO 8601

// Custom serializer is needed because documentation says the we MUST be prepare for new enums to be added to API.
private object GeocacheTypeSerializer : KSerializer<Geocache.Type> {
    override val descriptor = PrimitiveSerialDescriptor("type", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Geocache.Type) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Geocache.Type {
        val name = decoder.decodeString()
        return when (name) {
            "Traditional" -> Geocache.Type.Traditional
            "Multi" -> Geocache.Type.Multi
            "Moving" -> Geocache.Type.Moving
            "Quiz" -> Geocache.Type.Quiz
            "Own" -> Geocache.Type.Own
            "Webcam" -> Geocache.Type.Webcam
            "Other" -> Geocache.Type.Other
            else -> Geocache.Type.Other
        }
    }
}

/**
 * Structure is documented in [the docs](https://opencaching.pl/okapi/services/caches/geocache.html)
 */
@Serializable
public data class Geocache(
    @SerialName("code") val code: String,
    @SerialName("name") val name: String,
    @SerialName("location") val location: Location,
    @SerialName("type") val type: Type,
    @SerialName("status") val status: Status,
    @SerialName("needs_maintenance") val needsMaintenance: Boolean?, // docs don't mention this, but it's actually nullable
    @SerialName("url") val url: String,
    @SerialName("owner") val owner: User,
    @SerialName("gc_code") val gcCode: String?,
    // @SerialName("distance") val distance: Float?, // Requires my_location parameter to be provided
    // @SerialName("bearing") val bearing: Float?, // Requires my_location parameter to be provided
    // @SerialName("bearing2") val bearing2: Float?, // Requires my_location parameter to be provided
    // @SerialName("bearing3") val bearing3: Float?, // Requires my_location parameter to be provided
    // @SerialName("is_found") val isFound: Boolean, // Requires user_uuid param, or Auth level 3
    // @SerialName("is_not_found") val isNotFound: Boolean, // Requires user_uuid param, or Auth level 3
    // @SerialName("is_watched") val isWatched: Boolean, // Requires Auth level 3
    // @SerialName("is_ignored") val isIgnored: Boolean, // Requires Auth level 3
    @SerialName("founds") val founds: Int,
    @SerialName("notfounds") val notFounds: Int,
    @SerialName("willattends") val willattends: Int,
    @SerialName("watchers") val watchers: Int,
    @SerialName("size2") val size: Size,
    @SerialName("oxsize") val oxSize: Float, // undocumented and may change without notice
    @SerialName("difficulty") val difficulty: Float,
    @SerialName("terrain") val terrain: Float,
    @SerialName("trip_time") val tripTimeHours: Float?,
    @SerialName("trip_distance") val tripDistanceKm: Float?,
    @SerialName("rating") val rating: Float?,
    @SerialName("recommendations") val recommendations: Int,
    // @SerialName("is_recommended") val isRecommended: Boolean, // Requires Auth level 3
    @SerialName("short_description") val shortDescription: String,
    @SerialName("description") val description: String,
    @SerialName("descriptions") val descriptions: Map<String, String>,
    @SerialName("hint2") val hint2: String,
    @SerialName("hints2") val hints2: Map<String, String>,
    // @SerialName("preview_image") val previewImage: Image?, // OCDE only
    @SerialName("attr_acodes") val attributeCodes: List<String>,
    @SerialName("attrnames") val atrributeNames: List<String>,
    // @SerialName("oc_team_annotation") val ocTeamAnnotation: String // OCPL only
    // attribution_note
    // latest_logs
    // my_notes // Requires Auth level 3
    @SerialName("trackables_count") val trackablesCount: Int,
    @SerialName("trackables") val trackables: List<Trackable>,
    // alt_wpts
    // country2
    // region
    // protection_areas
    @SerialName("last_found") val lastFound: Instant?,
    @SerialName("last_modified") val lastModified: Instant,
    @SerialName("date_created") val dateCreated: Instant,
    @SerialName("date_hidden") val dateHidden: Instant,
) {
    public companion object {
        public val allParams: String = listOf(
            "code|name|location|type|status|needs_maintenance|url|owner|gc_code", // Basics (part 1)
            // "distance|bearing|bearing2|bearing3", // Requires my_location parameter to be provided
            "founds|notfounds|willattends|watchers|size2|oxsize|difficulty|terrain|trip_time|trip_distance", // Basic (part 2)
            "rating|recommendations|short_description|description|descriptions|hint2|hints2", // Basic (part 3)
            "attr_acodes|attrnames|trackables_count|trackables", // Basic (part 4)
            "last_found|last_modified|date_created|date_hidden", // Basic (part 5)
        ).joinToString("|")
    }

    @Serializable(with = GeocacheTypeSerializer::class)
    public enum class Type {
        Traditional,
        Multi,
        Quiz,
        Moving,
        Virtual,
        Webcam,
        Other,
        Event,

        // More types are in use at some installations
        Own,
        Podcast,
    }

    @Serializable
    public enum class Status {
        @SerialName("Available")
        Available,

        @SerialName("Temporarily unavailable")
        TemporarilyUnavailable,

        @SerialName("Archived")
        Archived,
    }

    public enum class Size(public val value: Int) {
        @SerialName("none")
        None(0),

        @SerialName("nano")
        Nano(1),

        @SerialName("micro")
        Micro(2),

        @SerialName("small")
        Small(3),

        @SerialName("regular")
        Regular(4),

        @SerialName("large")
        Large(5),

        @SerialName("xlarge")
        ExtraLarg(6),

        @SerialName("other")
        Other(0);

        public fun toFloat(): Float = value.toFloat()
    }

    @Serializable
    public data class Trackable(
        val code: String,
        val name: String,
        val url: String?,
    )
}

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
}

// Custom serializer is needed because documentation says the we MUST be prepare for new enums to be added to API.
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

/**
 * Represents both a [Geocache] image and a [Log] image.
 */
@Serializable
public data class Image(
    @SerialName("uuid") val uuid: String,
    @SerialName("url") val url: String,
    @SerialName("thumb_url") val thumbUrl: String,
    @SerialName("caption") val caption: String,
    // @SerialName("unique_caption") val uniqueCaption: String,
    @SerialName("is_spoiler") val spoiler: Boolean,
)

/**
 * Format is described [in the docs](https://opencaching.pl/okapi/introduction.html).
 */
@Serializable
public data class Error(
    val error: BasicError,
) {
    @Serializable
    public data class BasicError(
        @SerialName("developer_message") val developerMessage: String,
        @SerialName("reason_stack") val reasonStack: List<String>,
        @SerialName("status") val status: Int,
        @SerialName("more_info") val moreInfoUrl: String,
    )
}
