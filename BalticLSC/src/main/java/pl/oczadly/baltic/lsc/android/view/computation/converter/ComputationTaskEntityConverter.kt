package pl.oczadly.baltic.lsc.android.view.computation.converter

import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.computation.dto.Task

class ComputationTaskEntityConverter {

    fun convertFromTaskDTO(task: Task, appReleaseByUid: Map<String, AppReleaseEntity>): ComputationTaskEntity = ComputationTaskEntity(
        task.uid,
        task.parameters.taskName,
        task.releaseUid,
        appReleaseByUid[task.releaseUid]?.versionName ?: "",
        task.start,
        task.finish,
        task.status,
        task.parameters.priority
    )

}