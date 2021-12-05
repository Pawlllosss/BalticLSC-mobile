package pl.oczadly.baltic.lsc.app.dto.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pl.oczadly.baltic.lsc.app.dto.dataset.DatasetBinding

object DatasetBindingSerializer : KSerializer<DatasetBinding> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DatasetBinding", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DatasetBinding) =
        encoder.encodeInt(value.binding)

    override fun deserialize(decoder: Decoder): DatasetBinding {
        val binding = decoder.decodeInt()
        return DatasetBinding.values().asList()
            .find { it.binding == binding } ?: DatasetBinding.UNDEFINED
    }
}