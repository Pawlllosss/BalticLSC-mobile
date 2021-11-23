package pl.oczadly.baltic.lsc.android.view.computation

import java.time.LocalDateTime
import pl.oczadly.baltic.lsc.computation.dto.ComputationStatus

data class ComputationTaskEntity(
    val name: String,
    val version: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val status: ComputationStatus,
    val priority: Int
)