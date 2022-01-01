package pl.oczadly.baltic.lsc.android.view.app.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity

fun createUpdatedOnText(app: AppListItemEntity): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val newestReleaseDate = getNewestReleaseDate(app)
    val formattedDate: String =
        newestReleaseDate?.format(formatter) ?: ""
    return "Updated on $formattedDate"
}

private fun getNewestReleaseDate(app: AppListItemEntity): LocalDateTime? = app.releases
    .maxByOrNull { it.date }?.date
