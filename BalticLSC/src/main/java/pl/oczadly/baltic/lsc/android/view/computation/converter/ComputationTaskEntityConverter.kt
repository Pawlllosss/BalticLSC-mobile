package pl.oczadly.baltic.lsc.android.view.computation.converter

import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.app.dto.list.AppRelease
import pl.oczadly.baltic.lsc.computation.dto.Task

class ComputationTaskEntityConverter {

    fun convertFromTaskDTO(task: Task, appReleaseByUid: Map<String, AppRelease>): ComputationTaskEntity = ComputationTaskEntity(
        task.uid,
        task.parameters.taskName,
        task.releaseUid,
        appReleaseByUid[task.releaseUid]?.version ?: "",
        task.start,
        task.finish,
        task.status,
        task.parameters.priority
    )

}