package pl.oczadly.baltic.lsc.android.view.computation.adapter

import android.content.Context
import android.graphics.Color
import android.widget.Button
import pl.oczadly.baltic.lsc.computation.action.ComputationAction


class ComputationActionsButtonCreator {

    fun createButtonsForActions(actions: List<ComputationAction>, context: Context): List<Button> {
        return actions.map {
            val button = Button(context)
            button.text = it.description
            button.setBackgroundColor(if (it == ComputationAction.ABORT) Color.RED else Color.GREEN)
            button
        }
    }
}