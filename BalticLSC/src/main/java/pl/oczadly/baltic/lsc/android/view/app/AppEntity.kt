package pl.oczadly.baltic.lsc.android.view.app

import java.util.*
import kotlinx.datetime.LocalDateTime

data class AppEntity(
    val uid: UUID,
    val name: String,
    val icon: String,
    val updateDate: LocalDateTime,
    val description: String?
)
