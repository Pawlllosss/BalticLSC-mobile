package pl.oczadly.baltic.lsc.android.view.computation.entity

import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity

data class ComputationTaskGroup(val application: AppListItemEntity, val tasks: List<ComputationTaskEntity>)
