package pl.oczadly.baltic.lsc.android.view.dataset.adapter

import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity

class DatasetAdapter(
    private val datasetEntities: MutableList<DatasetEntity>
) :
    RecyclerView.Adapter<DatasetAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.dataset_name_text_view)
        val multiplicityTextView: TextView = view.findViewById(R.id.dataset_multiplicity_text_view)
        val dataTypeTextView: TextView = view.findViewById(R.id.dataset_data_type_text_view)
        val accessTypeTextView: TextView = view.findViewById(R.id.dataset_access_type_text_view)
        val dataStructureLabelTextView: TextView =
            view.findViewById(R.id.dataset_data_structure_label_text_view);
        val dataStructureTextView: TextView =
            view.findViewById(R.id.dataset_data_structure_text_view)
        val accessValuesTextView: TextView = view.findViewById(R.id.dataset_access_values_text_view)
        val pathValuesTextView: TextView = view.findViewById(R.id.dataset_path_values_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val dataset = datasetEntities[position]

        holder.nameTextView.text = dataset.name
        holder.multiplicityTextView.text = dataset.multiplicity.description
        holder.dataTypeTextView.text = dataset.dataType.name
        holder.accessTypeTextView.text = dataset.accessTypeEntity.name

        val dataStructure = dataset.dataStructure
        if (dataStructure != null) {
            holder.dataStructureLabelTextView.visibility = VISIBLE
            holder.dataStructureTextView.text = dataset.dataType.name
        }
        holder.dataTypeTextView.text = dataset.dataType.name
        holder.accessValuesTextView.text = dataset.accessValues
        holder.pathValuesTextView.text = dataset.pathValues
    }

    override fun getItemCount() = datasetEntities.size

    fun updateData(datasetEntities: List<DatasetEntity>) {
        this.datasetEntities.clear()
        this.datasetEntities.addAll(datasetEntities)
        notifyDataSetChanged()
    }
}