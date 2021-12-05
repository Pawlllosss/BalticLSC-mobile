package pl.oczadly.baltic.lsc.app.dto.dataset

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.app.dto.serializer.DatasetBindingSerializer

@Serializable(with = DatasetBindingSerializer::class)
enum class DatasetBinding(val binding: Int) {
    REQUIRED(0),
    UNKNOWN_1(1),
    PROVIDED(2),
    UNDEFINED(-1000)
}
