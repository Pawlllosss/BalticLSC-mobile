package pl.oczadly.baltic.lsc.app.dto.list

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.app.dto.dataset.DatasetPin

@Serializable
data class AppRelease(
    val description: String?,
    val uid: String,
    val diagramUid: String?,
    val version: String,
    val status: ReleaseStatus,
    val date: LocalDateTime,
    val openSource: Boolean,
    val pins: List<DatasetPin>,
    val supportedResourcesRange: ResourceRange
)
