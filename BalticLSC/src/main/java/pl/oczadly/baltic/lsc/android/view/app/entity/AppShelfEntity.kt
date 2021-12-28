package pl.oczadly.baltic.lsc.android.view.app.entity

import kotlinx.datetime.LocalDateTime

data class AppShelfEntity(
    val unitUid: String,
    val releaseUid: String,
    val name: String,
    val icon: String,
    val updateDate: LocalDateTime,
    val description: String?,
    val pins: List<DatasetPinEntity>
)
