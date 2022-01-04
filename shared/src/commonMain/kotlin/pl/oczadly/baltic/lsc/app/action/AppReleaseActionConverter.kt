package pl.oczadly.baltic.lsc.app.action

import pl.oczadly.baltic.lsc.app.dto.list.ReleaseStatus

class AppReleaseActionConverter {

    fun getActionsBasedOnStatus(
        status: ReleaseStatus
    ): List<AppReleaseAction> {
        if (status !in listOf(ReleaseStatus.CREATED, ReleaseStatus.APPROVED)) {
            return emptyList()
        }
        return listOf(AppReleaseAction.EDIT, AppReleaseAction.DELETE)
    }
}

