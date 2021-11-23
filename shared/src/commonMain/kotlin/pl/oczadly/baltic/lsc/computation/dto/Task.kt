package pl.oczadly.baltic.lsc.computation.dto

import kotlinx.datetime.LocalDateTime

data class Task(
    val uid: String,
    val releaseUid: String,// it's based on the order of uid return from the Shelf (within object, not unit!)
    val status: ComputationStatus,
    val start: LocalDateTime,
    val finish: LocalDateTime,
    val consumedCredits: Int,
    val parameters: TaskParameters
)
