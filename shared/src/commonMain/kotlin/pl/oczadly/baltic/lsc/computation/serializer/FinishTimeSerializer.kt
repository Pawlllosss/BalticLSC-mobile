package pl.oczadly.baltic.lsc.computation.serializer

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object FinishTimeSerializer : KSerializer<Instant?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant?) =
        encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): Instant? {
        val instantAsString = decoder.decodeString()

        return if (instantAsString == "0001-01-01T00:00:00") {
            null
        } else {
            Instant.parse(instantAsString)
        }
    }
}