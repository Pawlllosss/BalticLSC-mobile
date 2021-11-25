package pl.oczadly.baltic.lsc.computation.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pl.oczadly.baltic.lsc.computation.dto.ComputationStatus

object ComputationStatusSerializer : KSerializer<ComputationStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ComputationStatus", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ComputationStatus) =
        encoder.encodeString(value.name)

    override fun deserialize(decoder: Decoder): ComputationStatus {
        val status = decoder.decodeInt()
        return ComputationStatus.values().asList()
            .find { it.status == status } ?: ComputationStatus.UNDEFINED
    }
}