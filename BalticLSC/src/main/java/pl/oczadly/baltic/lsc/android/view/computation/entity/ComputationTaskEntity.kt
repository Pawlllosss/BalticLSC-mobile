package pl.oczadly.baltic.lsc.android.view.computation.entity

import java.io.Serializable
import kotlinx.datetime.Instant
import pl.oczadly.baltic.lsc.computation.dto.ComputationStatus

data class ComputationTaskEntity(
    val uid: String,
    val name: String,
    val releaseUid: String,
    val version: String,
    val startTime: Instant,
    val endTime: Instant?,
    val status: ComputationStatus,
    val priority: Int
): Serializable