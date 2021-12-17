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
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationTaskAdd
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskGroup
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity

class ComputationTaskGroupAdapter(
    private val taskGroups: MutableList<ComputationTaskGroup>,
    private val appShelfEntityByReleaseUid: MutableMap<String, AppShelfEntity>,
    private val datasetShelfEntitiesByDataTypeUid: MutableMap<String, List<DatasetEntity>>
) :
    RecyclerView.Adapter<ComputationTaskGroupAdapter.ItemViewHolder>() {

    class ItemViewHolder(
        view: View,
        appShelfEntityByReleaseUid: Map<String, AppShelfEntity>,
        datasetEntitiesByDataTypeUid: Map<String, List<DatasetEntity>>
    ) : RecyclerView.ViewHolder(view) {
        val appNameTextView: TextView = view.findViewById(R.id.computation_task_group_app_name)
        val computationTaskAddButton: FloatingActionButton =
            view.findViewById(R.id.computation_task_add_button)
        val computationTaskRecyclerView: RecyclerView =
            view.findViewById(R.id.computation_task_recycler_view)
        val computationTaskAdapter: ComputationTaskAdapter
        val context = itemView.context

        init {
            computationTaskAdapter = ComputationTaskAdapter(
                context,
                appShelfEntityByReleaseUid,
                datasetEntitiesByDataTypeUid
            )
            computationTaskRecyclerView.adapter = computationTaskAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val computationGroupsListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.computation_task_group_list_item, parent, false)

        return ItemViewHolder(
            computationGroupsListView,
            appShelfEntityByReleaseUid,
            datasetShelfEntitiesByDataTypeUid
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val taskGroup = taskGroups[position]
        holder.appNameTextView.text = taskGroup.application.name

        holder.computationTaskAddButton.findViewById<FloatingActionButton>(R.id.computation_task_add_button)
            .setOnClickListener {
                val intent = Intent(holder.context, ComputationTaskAdd::class.java)
                intent.putExtra("appListItemEntity", taskGroup.application)
                holder.context.startActivity(intent)
            }

        holder.computationTaskRecyclerView.layoutManager =
            LinearLayoutManager(holder.context, LinearLayoutManager.VERTICAL, false)
        holder.computationTaskAdapter.setTasks(taskGroup.tasks)
    }

    override fun getItemCount() = taskGroups.size

    fun updateData(
        taskGroups: List<ComputationTaskGroup>,
        appShelfEntityByReleaseUid: Map<String, AppShelfEntity>,
        datasetShelfEntitiesByDataTypeUid: Map<String, List<DatasetEntity>>
    ) {
        this.taskGroups.clear()
        this.taskGroups.addAll(taskGroups)
        this.appShelfEntityByReleaseUid.clear()
        this.appShelfEntityByReleaseUid.putAll(appShelfEntityByReleaseUid)
        this.datasetShelfEntitiesByDataTypeUid.clear()
        this.datasetShelfEntitiesByDataTypeUid.putAll(datasetShelfEntitiesByDataTypeUid)
        notifyDataSetChanged()
    }
}