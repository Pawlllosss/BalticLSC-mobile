package pl.oczadly.baltic.lsc.android.view.computation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity

class ComputationTaskAdapter(private val context: Context) :
    RecyclerView.Adapter<ComputationTaskAdapter.ItemViewHolder>() {

    private var tasks: List<ComputationTaskEntity> = ArrayList()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskNameTextView: TextView = view.findViewById(R.id.computation_task_name_text_view)
        val startDateTextView: TextView =
            view.findViewById(R.id.computation_task_start_date_text_view)
        val endDateTextView: TextView = view.findViewById(R.id.computation_task_end_date_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val computationListView = LayoutInflater.from(context)
            .inflate(R.layout.computation_task_list_item, parent, false)

        return ItemViewHolder(computationListView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskNameTextView.text = task.name
        holder.startDateTextView.text = task.startTime.toString()
        holder.endDateTextView.text = task.endTime.toString()
    }

    override fun getItemCount() = tasks.size

    fun setTasks(data: List<ComputationTaskEntity>) {
        if (tasks != data) {
            tasks = data
            notifyDataSetChanged()
        }
    }
}