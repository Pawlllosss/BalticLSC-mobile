package pl.oczadly.baltic.lsc.android.view.app.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.lazyPromise


class AppService(private val appApi: AppApi) {

    fun createFetchAppListPromise() = lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext appApi.fetchApplicationList().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }

    fun createFetchAppShelfPromise() = lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext appApi.fetchApplicationShelf().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }
}