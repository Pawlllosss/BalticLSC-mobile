package pl.oczadly.baltic.lsc.computation

import pl.oczadly.baltic.lsc.model.NoDataResponse

class ComputationService(private val computationApi: ComputationApi) {

    suspend fun startComputationTask(
        taskUid: String,
        datasetUidByPinUid: Map<String, String>
    ): NoDataResponse {
        datasetUidByPinUid.forEach {
            computationApi.injectDataToComputationTask(taskUid, it.key, it.value)
        }

        // TODO: figure out a way to return a list of async responses
        return NoDataResponse(true, "")
    }
}