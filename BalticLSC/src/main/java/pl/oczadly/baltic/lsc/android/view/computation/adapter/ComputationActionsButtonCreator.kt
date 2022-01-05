package pl.oczadly.baltic.lsc.android.view.computation.adapter

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.util.convertDpToPixels
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationTaskAbort
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationTaskArchive
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationTaskStart
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.computation.action.ComputationAction


class ComputationActionsButtonCreator {

    fun createButtonsForActions(
        actions: List<ComputationAction>,
        task: ComputationTaskEntity,
        appShelfEntity: AppShelfEntity?,
        datasetShelfEntitiesByDataTypeUid: Map<String, List<DatasetEntity>>,
        context: Context
    ): List<Button> {
        val buttonWeight: Float = 1.0f / actions.size
        return actions.map { computationAction ->
            val layoutParameters = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                buttonWeight
            )
            val density = context.resources.displayMetrics.density
            layoutParameters.leftMargin = convertDpToPixels(5, density)
            layoutParameters.rightMargin = convertDpToPixels(5, density)
            val button = Button(context)
            button.layoutParams = layoutParameters
            button.setTextColor(ContextCompat.getColor(context, R.color.cardview_light_background))
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            button.text = computationAction.description

            when (computationAction) {
                ComputationAction.START -> {
                    button.setOnClickListener {
                        val intent = Intent(context, ComputationTaskStart::class.java)
                        intent.putExtra("computationTaskName", task.name)
                        intent.putExtra("computationTaskUid", task.uid)
                        intent.putExtra(
                            "datasetShelfEntitiesByDataTypeUid",
                            HashMap<String, List<DatasetEntity>>(
                                datasetShelfEntitiesByDataTypeUid
                            )
                        )
                        appShelfEntity?.let {
                            intent.putExtra("datasetPins", ArrayList(it.pins))
                        }
                        context.startActivity(intent)
                    }
                }
                ComputationAction.ABORT -> {
                    button.setOnClickListener {
                        val intent = Intent(context, ComputationTaskAbort::class.java)
                        intent.putExtra("computationTaskName", task.name)
                        intent.putExtra("computationTaskUid", task.uid)
                        context.startActivity(intent)
                    }
                }
                ComputationAction.ARCHIVE -> {
                    button.setOnClickListener {
                        val intent = Intent(context, ComputationTaskArchive::class.java)
                        intent.putExtra("computationTaskName", task.name)
                        intent.putExtra("computationTaskUid", task.uid)
                        context.startActivity(intent)
                    }
                }
            }
            button
        }
    }
}