package pl.oczadly.baltic.lsc.android.view.dataset.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.computation.dto.TaskCreate
import pl.oczadly.baltic.lsc.lazyPromise

class DatasetAdd : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val computationApi = ComputationApi(MainActivity.state)

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataTypes = intent.getSerializableExtra(DatasetView.dataTypeListIntent) as? List<DataTypeEntity>
        val dataStructures = intent.getSerializableExtra(DatasetView.dataStructureListIntent) as? List<DataStructureEntity>
        val accessTypes = intent.getSerializableExtra(DatasetView.accessTypeListIntent) as? List<AccessTypeEntity>
        if (accessTypes == null || dataTypes == null || dataStructures == null) {
            finish()
        } else {
            setContentView(R.layout.activity_dataset_add)
            setSupportActionBar(findViewById(R.id.toolbar))

            val dataTypeSpinner = findViewById<Spinner>(R.id.dataset_data_type_spinner)
            val dataTypeAdapter: ArrayAdapter<DataTypeEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataTypes)
            dataTypeSpinner.adapter = dataTypeAdapter
            val dataStructureTextView = findViewById<TextView>(R.id.dataset_add_data_structure_label_text_view)
            val dataStructureSpinner = findViewById<Spinner>(R.id.dataset_data_structure_spinner)
            dataTypeSpinner.onItemSelectedListener = getOnItemSelectedListener(dataStructureTextView, dataStructureSpinner, dataStructures)

            val accessTypeSpinner = findViewById<Spinner>(R.id.dataset_access_type_spinner)
            val accessTypeAdapter: ArrayAdapter<AccessTypeEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accessTypes)
            accessTypeSpinner.adapter = accessTypeAdapter


            findViewById<Button>(R.id.dataset_add_create_button)
                .setOnClickListener {
//                    sendCreateDatasetRequestAndFinish(versionSpinner)
                    finish()
                }

            findViewById<Button>(R.id.dataset_add_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    private fun getOnItemSelectedListener(textView: TextView, dataStructureSpinner: Spinner, dataStructures: List<DataStructureEntity>) = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View,
            position: Int,
            id: Long
        ) {
            val dataType = parent.selectedItem as DataTypeEntity

            if (dataType.isStructured) {
                textView.visibility = View.VISIBLE
                dataStructureSpinner.visibility = View.VISIBLE
                val dataStructureAdapter: ArrayAdapter<DataStructureEntity> =
                    ArrayAdapter(this@DatasetAdd, android.R.layout.simple_spinner_dropdown_item, dataStructures)
                dataStructureSpinner.adapter = dataStructureAdapter
            } else {
                textView.visibility = View.GONE
                dataStructureSpinner.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            return
        }
    }

    private fun sendCreateDatasetRequestAndFinish(versionSpinner: Spinner) {
        launch(Dispatchers.Main) {
            try {
                lazyPromise {
                    withContext(Dispatchers.IO) {
                        try {
                            val appReleaseEntity =
                                versionSpinner.selectedItem as AppReleaseEntity
                            val taskCreateDTO = getTaskCreateDTO(appReleaseEntity)
                            return@withContext computationApi.initiateComputationTask(
                                taskCreateDTO, appReleaseEntity.releaseUid
                            ).data
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return@withContext null
                        }
                    }
                }.value.await()
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getTaskCreateDTO(appReleaseEntity: AppReleaseEntity): TaskCreate {
        val taskName =
            findViewById<EditText>(R.id.computation_task_name_edit_text).text.toString().trim()
        val taskPriority =
            findViewById<EditText>(R.id.computation_task_priority_edit_text).text.toString().toInt()
        val reservedCredits =
            findViewById<EditText>(R.id.computation_task_reserved_credits_edit_text).text.toString()
                .toInt()
        val isPrivate = findViewById<CheckBox>(R.id.computation_task_is_private_checkbox).isChecked


        return TaskCreate(
            taskName,
            appReleaseEntity.releaseUid,
            taskPriority,
            reservedCredits,
            isPrivate,
            emptyList(),
            "strong",
            "",
            100,
            22,
            33,
            31,
            45,
            54,
            77,
            41,
            56,
            "Break"
        )
    }
}