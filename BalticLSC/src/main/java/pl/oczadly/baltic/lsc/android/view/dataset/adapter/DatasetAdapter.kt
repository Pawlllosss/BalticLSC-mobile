package pl.oczadly.baltic.lsc.android.view.dataset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity

class DatasetAdapter(
    private val datasetEntities: MutableList<DatasetEntity>,
    dataTypes: List<DataTypeEntity>,
    dataStructures: List<DataStructureEntity>,
    accessTypes: List<AccessTypeEntity>
) :
    RecyclerView.Adapter<DatasetAdapter.ItemViewHolder>() {

    private val buttonConfigurator = DatasetActionsButtonConfigurator(dataTypes, dataStructures, accessTypes)

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
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.dataset_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val dataset = datasetEntities[position]

        buttonConfigurator.setupButtons(holder.itemView, dataset)

        holder.nameTextView.text = dataset.name
        holder.multiplicityTextView.text = dataset.datasetMultiplicity.description
        holder.dataTypeTextView.text = dataset.dataType.name
        holder.accessTypeTextView.text = dataset.accessTypeEntity.name

        val dataStructure = dataset.dataStructure
        if (dataStructure != null) {
            holder.dataStructureLabelTextView.visibility = VISIBLE
            holder.dataStructureTextView.text = dataset.dataStructure.name
        } else {
            holder.dataStructureLabelTextView.visibility = GONE
            holder.dataStructureTextView.text = ""
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
