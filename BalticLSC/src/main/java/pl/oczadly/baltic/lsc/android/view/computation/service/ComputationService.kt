package pl.oczadly.baltic.lsc.android.view.computation.service

import pl.oczadly.baltic.lsc.android.util.awaitPromise
import pl.oczadly.baltic.lsc.android.util.createApiPromise
import pl.oczadly.baltic.lsc.android.util.createApiPromiseSingleResponse
import pl.oczadly.baltic.lsc.android.view.computation.converter.ClusterEntityConverter
import pl.oczadly.baltic.lsc.android.view.computation.entity.ClusterEntity
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.computation.dto.TaskCreate

class ComputationService(private val computationApi: ComputationApi, private val clusterConverter: ClusterEntityConverter) {

    suspend fun getClustersAvailableForTask(releaseUid: String): List<ClusterEntity> {
        val clusters = awaitPromise(createApiPromise { computationApi.fetchComputationTaskCluster(releaseUid).data })
        return clusters.map(clusterConverter::convertFromClusterDTO)
    }

    suspend fun createTask(taskCreate: TaskCreate, releaseUid: String): String? {
        return awaitPromise(createApiPromiseSingleResponse {
            computationApi.initiateComputationTask(
                taskCreate,
                releaseUid
            )
        })
    }
}