package pl.oczadly.baltic.lsc.android.view.dataset.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.util.addTextViewToViewGroup
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.dataset.DatasetApi
import pl.oczadly.baltic.lsc.dataset.dto.DatasetCreate
import pl.oczadly.baltic.lsc.dataset.dto.DatasetMultiplicity
import pl.oczadly.baltic.lsc.lazyPromise

class DatasetAdd : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val editTextByAccessValue: MutableMap<String, EditText> = mutableMapOf()
    private val editTextByPathValue: MutableMap<String, EditText> = mutableMapOf()
    private val gson = Gson()
    private val datasetApi = DatasetApi(MainActivity.state)

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataTypes =
            intent.getSerializableExtra(DatasetView.dataTypeListIntent) as? List<DataTypeEntity>
        val dataStructures =
            intent.getSerializableExtra(DatasetView.dataStructureListIntent) as? List<DataStructureEntity>
        val accessTypes =
            intent.getSerializableExtra(DatasetView.accessTypeListIntent) as? List<AccessTypeEntity>
        if (accessTypes == null || dataTypes == null || dataStructures == null) {
            finish()
        } else {
            setContentView(R.layout.activity_dataset_add)
            setSupportActionBar(findViewById(R.id.toolbar))

            val dataTypeSpinner = findViewById<Spinner>(R.id.dataset_add_data_type_spinner)
            val dataTypeAdapter: ArrayAdapter<DataTypeEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataTypes)
            dataTypeSpinner.adapter = dataTypeAdapter
            val dataStructureTextView =
                findViewById<TextView>(R.id.dataset_add_data_structure_label_text_view)
            val dataStructureSpinner =
                findViewById<Spinner>(R.id.dataset_add_data_structure_spinner)
            dataTypeSpinner.onItemSelectedListener = getDataTypeOnItemSelectedListener(
                dataStructureTextView,
                dataStructureSpinner,
                dataStructures
            )

            val accessTypeSpinner = findViewById<Spinner>(R.id.dataset_add_access_type_spinner)
            val accessTypeAdapter: ArrayAdapter<AccessTypeEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accessTypes)
            accessTypeSpinner.adapter = accessTypeAdapter
            accessTypeSpinner.onItemSelectedListener = getAccessTypeOnItemSelectedListener()

            findViewById<Button>(R.id.dataset_add_create_button)
                .setOnClickListener {
                    sendCreateDatasetRequestAndFinish()
                    finish()
                }

            findViewById<Button>(R.id.dataset_add_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    private fun getDataTypeOnItemSelectedListener(
        textView: TextView,
        dataStructureSpinner: Spinner,
        dataStructures: List<DataStructureEntity>
    ) = object : AdapterView.OnItemSelectedListener {
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
                    ArrayAdapter(
                        this@DatasetAdd,
                        android.R.layout.simple_spinner_dropdown_item,
                        dataStructures
                    )
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

    private fun getAccessTypeOnItemSelectedListener() =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val linearLayout =
                    findViewById<LinearLayout>(R.id.dataset_add_access_type_values_linear_layout)
                linearLayout.removeAllViews()
                editTextByAccessValue.clear()
                val accessType = parent.selectedItem as AccessTypeEntity
                // assuming all fields are string, therefore not checking map values
                accessType.accessFieldNameByType.keys.forEach {
                    addTextViewToViewGroup(it, linearLayout, this@DatasetAdd)
                    val editText = EditText(this@DatasetAdd)
                    linearLayout.addView(editText)
                    editTextByAccessValue[it] = editText
                }
                accessType.pathFieldNameByType.keys.forEach {
                    addTextViewToViewGroup(it, linearLayout, this@DatasetAdd)
                    val editText = EditText(this@DatasetAdd)
                    linearLayout.addView(editText)
                    editTextByPathValue[it] = editText
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }

    private fun sendCreateDatasetRequestAndFinish() {
        launch(Dispatchers.Main) {
            try {
                lazyPromise {
                    withContext(Dispatchers.IO) {
                        try {
                            val datasetCreateDTO = getDatasetCreateDTO()
                            return@withContext datasetApi.addDataSet(datasetCreateDTO)
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

    private fun getDatasetCreateDTO(): DatasetCreate {
        val name =
            findViewById<EditText>(R.id.dataset_add_name_edit_text).text.toString().trim()
        val isMultiple =
            findViewById<CheckBox>(R.id.dataset_add_is_multiple_checkbox).isChecked
        val dataTypeSpinner = findViewById<Spinner>(R.id.dataset_add_data_type_spinner)
        val dataType = dataTypeSpinner.selectedItem as DataTypeEntity
        val dataStructureSpinner = findViewById<Spinner>(R.id.dataset_add_data_structure_spinner)
        val dataStructure = dataStructureSpinner.selectedItem as DataStructureEntity?
        val accessTypeSpinner = findViewById<Spinner>(R.id.dataset_add_access_type_spinner)
        val accessType = accessTypeSpinner.selectedItem as AccessTypeEntity

        return DatasetCreate(
            name,
            if (isMultiple) DatasetMultiplicity.MULTIPLE else DatasetMultiplicity.SINGLE,
            dataType.uid,
            dataType.name,
            dataType.version,
            dataStructure?.uid,
            dataStructure?.name,
            dataStructure?.version,
            accessType.uid,
            accessType.name,
            accessType.version,
            gson.toJson(createValueByFieldName(editTextByPathValue)),
            gson.toJson(createValueByFieldName(editTextByAccessValue))
        )
    }

    private fun createValueByFieldName(editTextByFieldName: Map<String, EditText>) =
        editTextByFieldName.map {
            val fieldName = it.key
            val fieldValue = it.value.text.toString().trim()
            fieldName to fieldValue
        }.toMap()
}