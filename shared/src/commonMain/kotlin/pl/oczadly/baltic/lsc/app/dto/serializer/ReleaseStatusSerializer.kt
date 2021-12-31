package pl.oczadly.baltic.lsc.app.dto.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pl.oczadly.baltic.lsc.app.dto.list.ReleaseStatus

object ReleaseStatusSerializer : KSerializer<ReleaseStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ReleaseStatus", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ReleaseStatus) =
        encoder.encodeInt(value.status)

    override fun deserialize(decoder: Decoder): ReleaseStatus {
        val status = decoder.decodeInt()
        return ReleaseStatus.values().asList()
            .find { it.status == status } ?: ReleaseStatus.UNDEFINED
    }
}
