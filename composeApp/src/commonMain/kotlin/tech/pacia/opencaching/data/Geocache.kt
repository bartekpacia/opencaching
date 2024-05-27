package tech.pacia.opencaching.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Geocache(
    val code: String,
    val name: String,
    val location: Location,
    val type: Type,
    val status: Status,
    val url: String,
    val owner: User,
) {
    enum class Type { Traditional, Multi, Moving, Quiz, Own, Webcam, Other }

    enum class Status { Available, TEMPORARILY_UNAVAILABLE, ARCHIVED }
}

@Serializable(with = LocationAsStringSerializer::class)
data class Location(
    val latitude: Double,
    val longitude: Double,
)

data class BoundingBox(
    val north: Double,
    val east: Double,
    val south: Double,
    val west: Double,
) {
    fun toPipeFormat() = "$south|$west|$north|$east"
}

object LocationAsStringSerializer : KSerializer<Location> {
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