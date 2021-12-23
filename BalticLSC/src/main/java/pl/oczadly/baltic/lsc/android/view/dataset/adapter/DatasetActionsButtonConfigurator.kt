package pl.oczadly.baltic.lsc.android.view.dataset.adapter

import android.content.Intent
import android.view.View
import android.widget.Button
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.dataset.activity.DatasetArchive
import pl.oczadly.baltic.lsc.android.view.dataset.activity.DatasetView
import pl.oczadly.baltic.lsc.android.view.dataset.activity.form.DatasetAdd
import pl.oczadly.baltic.lsc.android.view.dataset.activity.form.DatasetEdit
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity

class DatasetActionsButtonConfigurator(
    private val dataTypes: List<DataTypeEntity>,
    private val dataStructures: List<DataStructureEntity>,
    private val accessTypes: List<AccessTypeEntity>
) {

    fun setupButtons(view: View, datasetEntity: DatasetEntity) {
        val editButton = view.findViewById<Button>(R.id.dataset_edit_button)
        val copyButton = view.findViewById<Button>(R.id.dataset_copy_button)
        val archiveButton = view.findViewById<Button>(R.id.dataset_archive_button)
        val context = view.context

        editButton.setOnClickListener {
            val intent = Intent(context, DatasetEdit::class.java)
            intent.putExtra(DatasetView.datasetEntityIntent, datasetEntity)
            intent.putExtra(DatasetView.accessTypeListIntent, ArrayList(accessTypes))
            intent.putExtra(DatasetView.dataTypeListIntent, ArrayList(dataTypes))
            intent.putExtra(DatasetView.dataStructureListIntent, ArrayList(dataStructures))
            context.startActivity(intent)
        }
        copyButton.setOnClickListener {
            val intent = Intent(context, DatasetAdd::class.java)
            intent.putExtra(DatasetView.datasetEntityIntent, datasetEntity)
            intent.putExtra(DatasetView.accessTypeListIntent, ArrayList(accessTypes))
            intent.putExtra(DatasetView.dataTypeListIntent, ArrayList(dataTypes))
            intent.putExtra(DatasetView.dataStructureListIntent, ArrayList(dataStructures))
            context.startActivity(intent)
        }
        archiveButton.setOnClickListener {
            val intent = Intent(context, DatasetArchive::class.java)
            intent.putExtra(DatasetView.datasetEntityIntent, datasetEntity)
            context.startActivity(intent)
        }
    }
}
