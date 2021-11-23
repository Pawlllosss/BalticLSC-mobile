package pl.oczadly.baltic.lsc.computation.dto

import kotlinx.serialization.Serializable

@Serializable
enum class ComputationStatus(val status: Int) {
    IN_PROGRESS(1),
    COMPLETED(2),
    FAILED(3),
    UNKNOWN_3(4),
    UNKNOWN_4(5),
    UNKNOWN_5(6),
    UNKNOWN_6(7),
    UNKNOWN_7(8)
}