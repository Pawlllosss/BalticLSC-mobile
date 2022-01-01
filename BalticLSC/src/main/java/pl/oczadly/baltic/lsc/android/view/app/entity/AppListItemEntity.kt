package pl.oczadly.baltic.lsc.android.view.app.entity

import java.io.Serializable

data class AppListItemEntity(
    val uid: String,
    val diagramUid: String?,
    val releases: List<AppReleaseEntity>,
    val name: String,
    val iconUrl: String,
    val shortDescription: String?,
    val longDescription: String?,
    val pClass: String?,
    val keywords: List<String>?
): Serializable
