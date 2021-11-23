package pl.oczadly.baltic.lsc.computation.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val uid: String,
    val releaseUid: String,// it's based on the order of uid return from the Shelf (within object, not unit!)
    val status: Int, // TODO: serialize to enum based on int
//    val start: LocalDateTime, // TODO: format date
//    val finish: LocalDateTime,
    val consumedCredits: Double,
    val parameters: TaskParameters
)

/*
For example relaseUid is ImageClassTrainer_rel_001
we are taking uid from object ImageClassTrainer_rel_001
then we can redirect to app page based on unit uid da8417db-2302-45db-a779-337d3302e345
 */