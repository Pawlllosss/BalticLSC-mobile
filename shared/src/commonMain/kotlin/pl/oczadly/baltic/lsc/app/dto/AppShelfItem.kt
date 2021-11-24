package pl.oczadly.baltic.lsc.app.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AppShelfItem (
    val uid: String, // used to determine version of the application
    val diagramUid: String, // used to determine diagram for version of the application
    val unit: App,
    val version: String,
    val date: LocalDateTime
)