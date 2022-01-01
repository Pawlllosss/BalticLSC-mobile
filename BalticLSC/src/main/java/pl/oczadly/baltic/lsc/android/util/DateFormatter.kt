package pl.oczadly.baltic.lsc.android.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDate(localDateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return localDateTime.format(formatter) ?: ""
}
