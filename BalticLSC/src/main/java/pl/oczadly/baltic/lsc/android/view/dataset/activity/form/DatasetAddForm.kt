package pl.oczadly.baltic.lsc.android.view.dataset.activity.form

import com.google.gson.Gson
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.view.dataset.converter.AccessTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataStructureEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.service.DatasetService
import pl.oczadly.baltic.lsc.dataset.DatasetApi
import pl.oczadly.baltic.lsc.dataset.dto.DatasetCreate

class DatasetAddForm : DatasetForm() {

    private val datasetService = DatasetService(
        DatasetApi(MainActivity.state),
        DatasetEntityConverter(),
        DataTypeEntityConverter(),
        DataStructureEntityConverter(),
        AccessTypeEntityConverter(Gson())
    )

    override suspend fun sendDatasetRequest(datasetCreate: DatasetCreate): String? {
        return datasetService.addDataset(datasetCreate)
    }
}
