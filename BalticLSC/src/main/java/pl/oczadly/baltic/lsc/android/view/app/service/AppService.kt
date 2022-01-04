package pl.oczadly.baltic.lsc.android.view.app.service

import pl.oczadly.baltic.lsc.android.util.awaitPromise
import pl.oczadly.baltic.lsc.android.util.createApiPromise
import pl.oczadly.baltic.lsc.android.util.createApiPromiseNoDataResponse
import pl.oczadly.baltic.lsc.android.util.createApiPromiseSingleResponse
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.dto.AppEdit
import pl.oczadly.baltic.lsc.app.dto.AppReleaseEdit


class AppService(
    private val appApi: AppApi,
    private val appListItemEntityConverter: AppListItemEntityConverter,
    private val appShelfEntityConverter: AppShelfEntityConverter
) {

    suspend fun getAppList(): List<AppListItemEntity> {
        val applicationsList = awaitPromise(createApiPromise { appApi.fetchApplicationList().data })
        return applicationsList.map(appListItemEntityConverter::convertFromAppListItemDTO)
    }

    suspend fun getAppListItemByUid(appUid: String): AppListItemEntity {
        val appListItem = awaitPromise(
            createApiPromiseSingleResponse { appApi.fetchApplicationListItemByUid(appUid) })
        return appListItemEntityConverter.convertFromAppListItemDTO(appListItem!!)
    }

    suspend fun getAppShelf(): List<AppShelfEntity> {
        val applicationsShelf =
            awaitPromise(createApiPromise { appApi.fetchApplicationShelf().data })
        return applicationsShelf.map(appShelfEntityConverter::convertFromAppShelfItemDTO)
    }

    fun getReleasedAppList(appList: List<AppListItemEntity>): List<AppListItemEntity> {
        return appList.filter { it.releases.isNotEmpty() }
    }

    fun sortOwnedAppsFirst(
        appList: List<AppListItemEntity>,
        applicationsShelf: List<AppShelfEntity>
    ): List<AppListItemEntity> {
        val ownedAppsUids = applicationsShelf.map(AppShelfEntity::unitUid).toSet()
        return appList.sortedByDescending { ownedAppsUids.contains(it.uid) }
    }

    fun getOwnedAppList(
        appList: List<AppListItemEntity>,
        applicationsShelf: List<AppShelfEntity>
    ): List<AppListItemEntity> {
        val ownedReleasesUids = applicationsShelf.map(AppShelfEntity::releaseUid).toSet()
        return appList.map {
            AppListItemEntity(
                it.uid,
                it.diagramUid,
                getOwnedReleases(it.releases, ownedReleasesUids),
                it.name,
                it.iconUrl,
                it.shortDescription,
                it.longDescription,
                it.pClass,
                it.keywords
            )
        }
    }

    private fun getOwnedReleases(
        releases: List<AppReleaseEntity>,
        ownedReleasesUids: Set<String>
    ): List<AppReleaseEntity> = releases.filter { ownedReleasesUids.contains(it.releaseUid) }

    suspend fun createRelease(releaseName: String, appUid: String) {
        awaitPromise(createApiPromiseNoDataResponse {
            appApi.createAppRelease(
                releaseName,
                appUid
            )
        })
    }

    suspend fun deleteRelease(releaseUid: String) {
        awaitPromise(createApiPromiseNoDataResponse { appApi.deleteAppRelease(releaseUid) })
    }

    suspend fun editAppRelease(appReleaseEditDTO: AppReleaseEdit) {
        awaitPromise(createApiPromiseNoDataResponse { appApi.editAppRelease(appReleaseEditDTO) })
    }

    suspend fun addReleaseToCockpit(releaseUid: String) {
        awaitPromise(createApiPromiseNoDataResponse { appApi.addReleaseToCockpit(releaseUid) })
    }

    suspend fun deleteReleaseFromCockpit(releaseUid: String) {
        awaitPromise(createApiPromiseNoDataResponse { appApi.deleteReleaseFromCockpit(releaseUid) })
    }

    suspend fun createApp(appName: String) {
        awaitPromise(createApiPromiseNoDataResponse { appApi.createApp(appName) })
    }

    suspend fun editApp(appEditDTO: AppEdit) {
        awaitPromise(createApiPromiseNoDataResponse { appApi.editApp(appEditDTO) })
    }

    suspend fun deleteApp(appUid: String) {
        awaitPromise(createApiPromiseNoDataResponse { appApi.deleteApp(appUid) })
    }
}
