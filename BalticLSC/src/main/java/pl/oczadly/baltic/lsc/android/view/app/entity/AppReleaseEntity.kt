package pl.oczadly.baltic.lsc.android.view.app.entity

import pl.oczadly.baltic.lsc.app.dto.list.ReleaseStatus
import java.io.Serializable
import java.time.LocalDateTime

data class AppReleaseEntity(
    val releaseUid: String,
    val versionName: String,
    val releaseStatus: ReleaseStatus,
    val date: LocalDateTime
) : Serializable {
    override fun toString(): String {
        return versionName
    }
}
