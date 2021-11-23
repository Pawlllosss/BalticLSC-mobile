package pl.oczadly.baltic.lsc.android.view.computation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.oczadly.baltic.lsc.android.R

class ComputationAdapter(
    private val taskGroups: List<ComputationTaskGroup>,
    private val context: Context
) :
    RecyclerView.Adapter<ComputationAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appNameTextView: TextView = view.findViewById(R.id.computation_task_app_name)
        val tableLayout: TableLayout = view.findViewById(R.id.computation_task_table_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val computationListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.computation_list_item, parent, false)

        return ItemViewHolder(computationListView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val taskGroup = taskGroups[position]
        holder.appNameTextView.text = taskGroup.appName

        val rows = taskGroup.tasks.map {
            createTableRowForTask(it)
        }

        rows.forEach {
            holder.tableLayout.addView(it)
        }
    }

    override fun getItemCount() = taskGroups.size

    private fun createTableRowForTask(task: ComputationTaskEntity): TableRow {
        val tableRow = TableRow(context)
        val taskNameTextView = TextView(context)
        taskNameTextView.text = task.name
        val versionTextView = TextView(context)
        versionTextView.text = task.version
        val actionsTextView = TextView(context)
        actionsTextView.text = ""
        val statusTextView = TextView(context)
        statusTextView.text = task.status.name
        val priorityTextView = TextView(context)
        priorityTextView.text = task.priority.toString()

        tableRow.addView(taskNameTextView)
        tableRow.addView(versionTextView)
        tableRow.addView(actionsTextView)
        tableRow.addView(statusTextView)
        tableRow.addView(priorityTextView)

        return tableRow
    }
}