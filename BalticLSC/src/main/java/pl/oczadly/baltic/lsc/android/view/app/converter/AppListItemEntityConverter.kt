package pl.oczadly.baltic.lsc.android.view.app.converter

import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.app.dto.list.AppListItem

class AppListItemEntityConverter {

    private val appReleaseEntityConverter =
        AppReleaseEntityConverter(DataSetPinEntityConverter(), AppResourceRangeEntityConverter())

    fun convertFromAppListItemDTO(appListItem: AppListItem): AppListItemEntity = AppListItemEntity(
        appListItem.uid,
        appListItem.diagramUid,
        appListItem.releases.map(appReleaseEntityConverter::convertFromAppReleaseDTO),
        appListItem.name,
        appListItem.icon,
        appListItem.shortDescription,
        appListItem.longDescription,
        appListItem.pClass,
        appListItem.keywords
    )
}
