package pl.oczadly.baltic.lsc.android.view.computation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.computation.action.ComputationActionConverter

class ComputationTaskAdapter(
    private val context: Context,
    private val computationActionsConverter: ComputationActionConverter = ComputationActionConverter(),
    private val computationActionsButtonCreator: ComputationActionsButtonCreator = ComputationActionsButtonCreator()
) :
    RecyclerView.Adapter<ComputationTaskAdapter.ItemViewHolder>() {

    private var tasks: List<ComputationTaskEntity> = ArrayList()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskNameTextView: TextView = view.findViewById(R.id.computation_task_name_text_view)
        val startDateTextView: TextView =
            view.findViewById(R.id.computation_task_start_date_text_view)
        val endDateTextView: TextView = view.findViewById(R.id.computation_task_end_date_text_view)
        val priorityTextView: TextView = view.findViewById(R.id.computation_task_priority_text_view)
        val versionTextView: TextView = view.findViewById(R.id.computation_task_version_text_view)
        val statusTextView: TextView = view.findViewById(R.id.computation_task_status_text_view)
        val buttonsLinearLayout: LinearLayout =
            view.findViewById(R.id.computation_task_buttons_linear_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val computationListView = LayoutInflater.from(context)
            .inflate(R.layout.computation_task_list_item, parent, false)

        return ItemViewHolder(computationListView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskNameTextView.text = task.name
        holder.startDateTextView.text = formatDate(task.startTime)
        holder.endDateTextView.text = task.endTime?.let { formatDate(it) } ?: "-"
        holder.priorityTextView.text = task.priority.toString()
        holder.versionTextView.text = task.version
        val taskStatus = task.status
        holder.statusTextView.text = taskStatus.description

        val actions = computationActionsConverter.getActionsBasedOnStatus(taskStatus)
        val actionsButtons =
            computationActionsButtonCreator.createButtonsForActions(actions, task, context)
        actionsButtons.forEach(holder.buttonsLinearLayout::addView)
    }

    override fun getItemCount() = tasks.size

    fun setTasks(data: List<ComputationTaskEntity>) {
        if (tasks != data) {
            tasks = data
            notifyDataSetChanged()
        }
    }

    private fun formatDate(date: Instant): String =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
            .format(date.toJavaInstant())
}