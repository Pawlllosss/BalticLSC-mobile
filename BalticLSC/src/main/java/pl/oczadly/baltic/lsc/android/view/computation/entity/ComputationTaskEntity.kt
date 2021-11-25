package pl.oczadly.baltic.lsc.android.view.computation.entity

import kotlinx.datetime.Instant
import pl.oczadly.baltic.lsc.computation.dto.ComputationStatus

data class ComputationTaskEntity(
    val name: String,
    val version: String,
    val startTime: Instant,
    val endTime: Instant?,
    val status: ComputationStatus,
    val priority: Int
)