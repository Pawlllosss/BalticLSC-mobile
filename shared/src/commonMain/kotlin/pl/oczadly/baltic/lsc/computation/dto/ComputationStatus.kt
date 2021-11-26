package pl.oczadly.baltic.lsc.computation.dto

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.computation.serializer.ComputationStatusSerializer

@Serializable(with = ComputationStatusSerializer::class)
enum class ComputationStatus(val status: Int, val description: String) {
    IDLE(0, "Idle"),
    WORKING(1, "Working"),
    COMPLETED(2, "Completed"),
    FAILED(3, "Failed"),
    UNKNOWN_2(4, "Unknown"),
    UNKNOWN_3(5, "Unknown"),
    UNKNOWN_4(6, "Unknown"),
    UNKNOWN_5(7, "Unknown"),
    UNKNOWN_6(8, "Unknown"),
    UNDEFINED(-1000, "")
}