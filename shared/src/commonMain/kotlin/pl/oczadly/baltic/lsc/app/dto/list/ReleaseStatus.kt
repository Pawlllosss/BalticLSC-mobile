package pl.oczadly.baltic.lsc.app.dto.list

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.app.dto.serializer.ReleaseStatusSerializer

@Serializable(with = ReleaseStatusSerializer::class)
enum class ReleaseStatus(val status: Int, val description: String) {
    CREATED(0, "Created"),
    UNKNOWN_1(1, "Unknown_1"),
    APPROVED(2, "Approved"),
    DEPRECATED(3, "Deprecated"),
    UNDEFINED(-1000, "Undefined")
}
