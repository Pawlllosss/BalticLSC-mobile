package pl.oczadly.baltic.lsc.android.view.dataset.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetShelfEntity

class DatasetAdapter(
    private val datasetShelfEntities: MutableList<DatasetShelfEntity>
) :
    RecyclerView.Adapter<DatasetAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.dataset_name_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val dataset = datasetShelfEntities[position]

        holder.nameTextView.text = dataset.name
    }

    override fun getItemCount() = datasetShelfEntities.size
}