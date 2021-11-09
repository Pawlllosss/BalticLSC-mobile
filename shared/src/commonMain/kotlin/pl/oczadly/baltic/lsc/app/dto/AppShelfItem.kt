package pl.oczadly.baltic.lsc.app.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AppShelfItem (
    val unit: App,
    val date: LocalDateTime
)