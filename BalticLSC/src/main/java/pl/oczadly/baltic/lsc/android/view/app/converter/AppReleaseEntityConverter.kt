package pl.oczadly.baltic.lsc.android.view.app.converter

import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.app.dto.list.AppRelease

class AppReleaseEntityConverter {

    fun convertFromAppReleaseDTO(appRelease: AppRelease): AppReleaseEntity = AppReleaseEntity(
        appRelease.uid,
        appRelease.version,
        appRelease.status,
        appRelease.date
    )
}
