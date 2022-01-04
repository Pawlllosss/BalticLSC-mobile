package pl.oczadly.baltic.lsc.android.view.app.entity

import pl.oczadly.baltic.lsc.app.dto.list.ReleaseStatus
import java.io.Serializable
import java.time.LocalDateTime

data class AppReleaseEntity(
    val description: String?,
    val isOpenSource: Boolean,
    val releaseUid: String,
    val versionName: String,
    val releaseStatus: ReleaseStatus,
    val date: LocalDateTime,
    val datasetPins: List<DatasetPinEntity>,
    val resourceRange: AppResourceRangeEntity
) : Serializable {
    override fun toString(): String {
        return versionName
    }
}
