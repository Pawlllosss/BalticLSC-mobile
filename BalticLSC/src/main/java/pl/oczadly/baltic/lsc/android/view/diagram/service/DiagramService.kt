package pl.oczadly.baltic.lsc.android.view.diagram.service

import pl.oczadly.baltic.lsc.android.util.awaitPromise
import pl.oczadly.baltic.lsc.android.util.createApiPromiseSingleResponse
import pl.oczadly.baltic.lsc.android.view.diagram.converter.DiagramEntityConverter
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DiagramEntity
import pl.oczadly.baltic.lsc.diagram.DiagramApi


class DiagramService(
    private val diagramApi: DiagramApi,
    private val diagramEntityConverter: DiagramEntityConverter
) {

    suspend fun getDiagramByReleaseUid(releaseUid: String): DiagramEntity {
        val diagram = awaitPromise(createApiPromiseSingleResponse {
            diagramApi.fetchDiagramByReleaseUid(releaseUid)
        })!!
        return diagramEntityConverter.convertFromDataStructureDTO(diagram)
    }
}
