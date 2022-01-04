package pl.oczadly.baltic.lsc.android.view.app.converter

import kotlinx.datetime.toJavaLocalDateTime
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.app.dto.list.AppRelease

class AppReleaseEntityConverter(
    private val dataSetPinConverter: DataSetPinEntityConverter,
    private val resourceRangeConverter: AppResourceRangeEntityConverter
) {

    fun convertFromAppReleaseDTO(appRelease: AppRelease): AppReleaseEntity = AppReleaseEntity(
        appRelease.description,
        appRelease.openSource,
        appRelease.uid,
        appRelease.version,
        appRelease.status,
        appRelease.date.toJavaLocalDateTime(),
        appRelease.pins.map(dataSetPinConverter::convertFromDataSetPinDTO),
        resourceRangeConverter.convertFromResourceRangeDTO(appRelease.supportedResourcesRange)
    )
}
