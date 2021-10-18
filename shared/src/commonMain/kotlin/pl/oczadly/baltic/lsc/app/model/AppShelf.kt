package pl.oczadly.baltic.lsc.app.model

import kotlinx.serialization.Serializable

@Serializable
data class AppShelf (
    val unit: App
)