package pl.oczadly.baltic.lsc.android.view.app.util

import java.time.LocalDateTime
import pl.oczadly.baltic.lsc.android.util.formatDate
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity

fun createUpdatedOnText(app: AppListItemEntity): String {
    val newestReleaseDate = getNewestReleaseDate(app)
    val formattedDate: String =
        newestReleaseDate?.let { formatDate(it) } ?: ""
    return "Updated on $formattedDate"
}

private fun getNewestReleaseDate(app: AppListItemEntity): LocalDateTime? = app.releases
    .maxByOrNull { it.date }?.date
