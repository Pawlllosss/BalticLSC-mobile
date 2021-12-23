package pl.oczadly.baltic.lsc.android.view.dataset.service

import pl.oczadly.baltic.lsc.android.util.awaitPromise
import pl.oczadly.baltic.lsc.android.util.createApiPromise
import pl.oczadly.baltic.lsc.android.util.createApiPromiseNoDataResponse
import pl.oczadly.baltic.lsc.android.util.createApiPromiseSingleResponse
import pl.oczadly.baltic.lsc.android.view.dataset.converter.AccessTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataStructureEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.dataset.DatasetApi
import pl.oczadly.baltic.lsc.dataset.dto.DatasetCreate

class DatasetService(
    private val datasetApi: DatasetApi,
    private val datasetEntityConverter: DatasetEntityConverter,
    private val dataTypeEntityConverter: DataTypeEntityConverter,
    private val dataStructureEntityConverter: DataStructureEntityConverter,
    private val accessTypeEntityConverter: AccessTypeEntityConverter
) {

    suspend fun getDatasets(): List<DatasetEntity> {
        val datasetsShelf = awaitPromise(createApiPromise { datasetApi.fetchDatasetShelf().data })
        return datasetsShelf.map(datasetEntityConverter::convertFromDatasetShelfItemDTO)
    }

    suspend fun getDataTypes(): List<DataTypeEntity> {
        val dataTypes = awaitPromise(createApiPromise { datasetApi.fetchDataTypes().data })
        return dataTypes.map(dataTypeEntityConverter::convertFromDataTypeDTO)
    }

    suspend fun getDataStructures(): List<DataStructureEntity> {
        val dataStructures =
            awaitPromise(createApiPromise { datasetApi.fetchDataStructures().data })
        return dataStructures.map(dataStructureEntityConverter::convertFromDataStructureDTO)
    }

    suspend fun getAccessTypes(): List<AccessTypeEntity> {
        val accessTypes = awaitPromise(createApiPromise { datasetApi.fetchAccessTypes().data })
        return accessTypes.map(accessTypeEntityConverter::convertFromAccessTypeDTO)
    }

    suspend fun archiveDataset(datasetUid: String) {
        awaitPromise(createApiPromiseNoDataResponse { datasetApi.archiveDataset(datasetUid) })
    }

    suspend fun addDataset(datasetCreate: DatasetCreate): String? {
        return awaitPromise(createApiPromiseSingleResponse { datasetApi.addDataset(datasetCreate) })
    }

    suspend fun editDataset(datasetCreate: DatasetCreate): String? {
        return awaitPromise(createApiPromiseSingleResponse { datasetApi.updateDataset(datasetCreate) })
    }
}
