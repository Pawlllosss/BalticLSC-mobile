package pl.oczadly.baltic.lsc.android.view.dataset.service

import pl.oczadly.baltic.lsc.android.util.createApiPromise
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.dataset.DatasetApi

class DatasetService(private val datasetApi: DatasetApi, private val datasetEntityConverter: DatasetEntityConverter) {

    suspend fun getDatasetEntities(): List<DatasetEntity> {
        val datasetsShelf = createApiPromise { datasetApi.fetchDatasetShelf().data }.value.await()
        return datasetsShelf.map(datasetEntityConverter::convertFromDatasetShelfItemDTO)
    }

    fun createFetchDataTypesPromise() = createApiPromise { datasetApi.fetchDataTypes().data }

    fun createFetchDataStructuresPromise() = createApiPromise { datasetApi.fetchDataStructures().data }

    fun createFetchAccessTypesPromise() = createApiPromise { datasetApi.fetchAccessTypes().data }
}
