package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.dataset.serializer.DatasetMultiplicitySerializer

@Serializable(with = DatasetMultiplicitySerializer::class)
enum class DatasetMultiplicity(val value: Int, val description: String) {
    SINGLE(0, "Single"),
    MULTIPLE(1, "Multiple"),
    UNDEFINED(-1000, "Undefined")
}
