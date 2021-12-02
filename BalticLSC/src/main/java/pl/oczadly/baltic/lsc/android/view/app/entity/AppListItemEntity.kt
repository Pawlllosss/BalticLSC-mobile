package pl.oczadly.baltic.lsc.android.view.app.entity

data class AppListItemEntity(
    val uid: String,
    val diagramUid: String?,
    val releases: List<AppReleaseEntity>,
    val name: String,
    val shortDescription: String?,
    val longDescription: String?
)
