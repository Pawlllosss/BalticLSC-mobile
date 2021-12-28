package pl.oczadly.baltic.lsc.android.view.app.converter

import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem

class AppShelfEntityConverter {

    private val dataSetPinEntityConverter = DataSetPinEntityConverter()

    fun convertFromAppShelfItemDTO(appShelfItem: AppShelfItem): AppShelfEntity {
        val appUnit = appShelfItem.unit
        return AppShelfEntity(
            appUnit.uid,
            appShelfItem.uid,
            appUnit.name,
            appUnit.icon,
            appShelfItem.date,
            appUnit.shortDescription,
            appShelfItem.pins.map(dataSetPinEntityConverter::convertFromDataSetPinDTO)
        )
    }
}