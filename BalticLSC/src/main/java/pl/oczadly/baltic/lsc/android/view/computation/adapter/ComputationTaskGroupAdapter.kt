package pl.oczadly.baltic.lsc.android.view.computation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.computation.ComputationTaskAdd
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskGroup

class ComputationTaskGroupAdapter(private val taskGroups: List<ComputationTaskGroup>) :
    RecyclerView.Adapter<ComputationTaskGroupAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appNameTextView: TextView = view.findViewById(R.id.computation_task_group_app_name)
        val computationTaskAddButton: FloatingActionButton =
            view.findViewById(R.id.computation_task_add_button)
        val computationTaskRecyclerView: RecyclerView =
            view.findViewById(R.id.computation_task_recycler_view)
        val computationTaskAdapter: ComputationTaskAdapter
        val context = itemView.context

        init {
            computationTaskAdapter = ComputationTaskAdapter(context)
            computationTaskRecyclerView.adapter = computationTaskAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val computationGroupsListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.computation_task_group_list_item, parent, false)

        return ItemViewHolder(computationGroupsListView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val taskGroup = taskGroups[position]
        holder.appNameTextView.text = taskGroup.application.name

        holder.computationTaskAddButton.findViewById<FloatingActionButton>(R.id.computation_task_add_button)
            .setOnClickListener {
                val intent = Intent(holder.context, ComputationTaskAdd::class.java)
                // TODO: need to pass data through Intent
                holder.context.startActivity(intent)
            }

        holder.computationTaskRecyclerView.layoutManager =
            LinearLayoutManager(holder.context, LinearLayoutManager.VERTICAL, false)
        holder.computationTaskAdapter.setTasks(taskGroup.tasks)
    }

    override fun getItemCount() = taskGroups.size
}