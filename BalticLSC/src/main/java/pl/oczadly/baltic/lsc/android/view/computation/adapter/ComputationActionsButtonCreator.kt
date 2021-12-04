package pl.oczadly.baltic.lsc.android.view.computation.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationTaskAbort
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.computation.action.ComputationAction


class ComputationActionsButtonCreator {

    fun createButtonsForActions(
        actions: List<ComputationAction>,
        task: ComputationTaskEntity,
        context: Context
    ): List<Button> {
        return actions.map {
            val button = Button(context)
            button.text = it.description
            button.setBackgroundColor(if (it == ComputationAction.ABORT) Color.RED else Color.GREEN)
            when (it) {
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