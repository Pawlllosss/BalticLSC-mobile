package pl.oczadly.baltic.lsc.android.view.app.adapter

import android.content.Context
import android.graphics.Color
import android.widget.Button
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.app.action.AppReleaseAction

class ReleaseActionsButtonCreator {

    fun createButtonsForActions(
        actions: List<AppReleaseAction>,
        releaseEntity: AppReleaseEntity,
        context: Context
    ): List<Button> {
        return actions.map { action ->
            val button = Button(context)
            button.text = action.description

            when (action) {
                AppReleaseAction.ADD_COCKPIT -> {
                    button.setBackgroundColor(Color.GREEN)
                    button.setOnClickListener {
//                        val intent = Intent(context, ComputationTaskStart::class.java)
//                        intent.putExtra("computationTaskName", task.name)
//                        context.startActivity(intent)
                    }
                }
                AppReleaseAction.REMOVE_COCKPIT -> {
                    button.setBackgroundColor(Color.RED)
                    button.setOnClickListener {
//                        val intent = Intent(context, ComputationTaskStart::class.java)
//                        intent.putExtra("computationTaskName", task.name)
//                        context.startActivity(intent)
                    }
                }
                AppReleaseAction.ADD_TOOLBOX -> {
                    button.setBackgroundColor(Color.GREEN)
                    button.setOnClickListener {
//                        val intent = Intent(context, ComputationTaskStart::class.java)
//                        intent.putExtra("computationTaskName", task.name)
//                        context.startActivity(intent)
                    }
                }
                AppReleaseAction.REMOVE_TOOLBOX -> {
                    button.setBackgroundColor(Color.RED)
                    button.setOnClickListener {
//                        val intent = Intent(context, ComputationTaskStart::class.java)
//                        intent.putExtra("computationTaskName", task.name)
//                        context.startActivity(intent)
                    }
                }
                AppReleaseAction.EDIT -> {
                    button.setBackgroundColor(Color.BLUE)
                    button.setOnClickListener {
//                        val intent = Intent(context, ComputationTaskStart::class.java)
//                        intent.putExtra("computationTaskName", task.name)
//                        context.startActivity(intent)
                    }
                }
                AppReleaseAction.DELETE -> {
                    button.setBackgroundColor(Color.RED)
                    button.setOnClickListener {
//                        val intent = Intent(context, ComputationTaskStart::class.java)
//                        intent.putExtra("computationTaskName", task.name)
//                        context.startActivity(intent)
                    }
                }
            }
            button
        }
    }
}