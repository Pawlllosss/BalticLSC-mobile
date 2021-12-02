package pl.oczadly.baltic.lsc.app.dto.list

import kotlinx.serialization.Serializable

@Serializable
data class AppListItem(
    val uid: String,
    val diagramUid: String?,
    val releases: List<AppRelease>,
    val name: String,
    val shortDescription: String?,
    val longDescription: String?
)
