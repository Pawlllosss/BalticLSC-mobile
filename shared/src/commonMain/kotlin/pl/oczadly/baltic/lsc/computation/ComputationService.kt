package pl.oczadly.baltic.lsc.computation

import pl.oczadly.baltic.lsc.model.NoDataResponse

class ComputationService(private val computationApi: ComputationApi) {

    suspend fun startComputationTask(
        taskUid: String,
        datasetUidByPinUid: Map<String, String>
    ): List<NoDataResponse> =
        datasetUidByPinUid.map {
            computationApi.injectDataToComputationTask(taskUid, it.key, it.value)
    }
}
