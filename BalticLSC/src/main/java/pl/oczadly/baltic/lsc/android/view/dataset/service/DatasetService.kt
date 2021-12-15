package pl.oczadly.baltic.lsc.android.view.dataset.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.dataset.DatasetApi
import pl.oczadly.baltic.lsc.lazyPromise


class DatasetService(private val datasetApi: DatasetApi) {

    fun createFetchDatasetShelfPromise() = lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext datasetApi.fetchDatasetShelf().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }
}
