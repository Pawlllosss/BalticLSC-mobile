package pl.oczadly.baltic.lsc.app.action

enum class AppReleaseAction(val description: String) {
    ADD_COCKPIT("Add to Cockpit"),
    REMOVE_COCKPIT("Remove from Cockpit"),
    ADD_TOOLBOX("Add to Toolbox"),
    REMOVE_TOOLBOX("Remove from Toolbox"),
    EDIT("Edit"),
    DELETE("Delete"),
}
