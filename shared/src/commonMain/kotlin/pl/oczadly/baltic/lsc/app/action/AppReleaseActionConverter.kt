package pl.oczadly.baltic.lsc.app.action

import pl.oczadly.baltic.lsc.app.dto.list.ReleaseStatus

class AppReleaseActionConverter {

    fun getActionsBasedOnOwnership(
        isOwned: Boolean,
        isInToolbox: Boolean,
        status: ReleaseStatus
    ): List<AppReleaseAction> {
        if (status !in listOf(ReleaseStatus.CREATED, ReleaseStatus.APPROVED)) {
            return emptyList()
        }
        val actions = mutableListOf<AppReleaseAction>()
        if (isOwned) actions.add(AppReleaseAction.REMOVE_COCKPIT) else actions.add(AppReleaseAction.ADD_COCKPIT)
        if (isInToolbox) actions.add(AppReleaseAction.REMOVE_TOOLBOX) else actions.add(
            AppReleaseAction.ADD_TOOLBOX
        )
        actions.addAll(listOf(AppReleaseAction.EDIT, AppReleaseAction.DELETE))

        return actions
    }
}
