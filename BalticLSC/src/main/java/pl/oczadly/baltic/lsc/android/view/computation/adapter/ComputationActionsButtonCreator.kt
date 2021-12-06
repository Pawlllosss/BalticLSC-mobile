package pl.oczadly.baltic.lsc.android.view.computation.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationTaskAbort
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationTaskStart
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetShelfEntity
import pl.oczadly.baltic.lsc.computation.action.ComputationAction


class ComputationActionsButtonCreator {

    fun createButtonsForActions(
        actions: List<ComputationAction>,
        task: ComputationTaskEntity,
        appShelfEntity: AppShelfEntity?,
        datasetShelfEntitiesByDataTypeUid: Map<String, List<DatasetShelfEntity>>,
        context: Context
    ): List<Button> {
        return actions.map { computationAction ->
            val button = Button(context)
            button.text = computationAction.description
            button.setBackgroundColor(if (computationAction == ComputationAction.ABORT) Color.RED else Color.GREEN)
            when (computationAction) {
                ComputationAction.START -> button.setOnClickListener {
                    val intent = Intent(context, ComputationTaskStart::class.java)
                    intent.putExtra("computationTaskName", task.name)
                    // TODO: send datasets
                    appShelfEntity?.let {
                        intent.putExtra("datasetPins", ArrayList(it.pins))
                    }
                    context.startActivity(intent)
                }
                ComputationAction.ABORT -> button.setOnClickListener {
                    val intent = Intent(context, ComputationTaskAbort::class.java)
                    intent.putExtra("computationTaskName", task.name)
                    intent.putExtra("computationTaskUid", task.uid)
                    context.startActivity(intent)
                }
            }
            button
        }
    }
}