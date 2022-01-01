package pl.oczadly.baltic.lsc.app.dto.list

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AppRelease(
    val description: String?,
    val uid: String,
    val diagramUid: String?,
    val version: String,
    val status: ReleaseStatus,
    val date: LocalDateTime,
    val openSource: Boolean
)
