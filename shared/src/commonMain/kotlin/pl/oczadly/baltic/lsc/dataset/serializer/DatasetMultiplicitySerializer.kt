package pl.oczadly.baltic.lsc.dataset.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pl.oczadly.baltic.lsc.dataset.dto.DatasetMultiplicity

object DatasetMultiplicitySerializer : KSerializer<DatasetMultiplicity> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DatasetMultiplicity", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DatasetMultiplicity) =
        encoder.encodeInt(value.value)

    override fun deserialize(decoder: Decoder): DatasetMultiplicity {
        val value = decoder.decodeInt()
        return DatasetMultiplicity.values().asList()
            .find { it.value == value } ?: DatasetMultiplicity.UNDEFINED
    }
}
