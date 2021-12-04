package pl.oczadly.baltic.lsc.android.view.app.entity

import java.io.Serializable

data class AppReleaseEntity(
    val releaseUid: String,
    val versionName: String
): Serializable {
    override fun toString(): String {
        return versionName
    }
}
