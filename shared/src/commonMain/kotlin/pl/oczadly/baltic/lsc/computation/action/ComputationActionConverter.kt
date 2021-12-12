package pl.oczadly.baltic.lsc.computation.action

import pl.oczadly.baltic.lsc.computation.dto.ComputationStatus

class ComputationActionConverter {

    fun getActionsBasedOnStatus(computationStatus: ComputationStatus) = when (computationStatus) {
        ComputationStatus.IDLE -> listOf(ComputationAction.START, ComputationAction.ABORT, ComputationAction.ARCHIVE)
        ComputationStatus.WORKING -> listOf(ComputationAction.INSERT, ComputationAction.ABORT)
        else -> listOf(ComputationAction.ARCHIVE)
    }
}