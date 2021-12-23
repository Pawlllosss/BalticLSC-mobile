package pl.oczadly.baltic.lsc.android.view.dataset.activity.form

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.util.addTextViewToViewGroup
import pl.oczadly.baltic.lsc.android.util.convertFromJsonStringToMap
import pl.oczadly.baltic.lsc.android.view.dataset.activity.DatasetView
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.dataset.dto.DatasetCreate
import pl.oczadly.baltic.lsc.dataset.dto.DatasetMultiplicity

abstract class DatasetForm : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val editTextByAccessValue: MutableMap<String, EditText> = mutableMapOf()
    private val editTextByPathValue: MutableMap<String, EditText> = mutableMapOf()
    private val gson = Gson()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val datasetEntity =
            intent.getSerializableExtra(DatasetView.datasetEntityIntent) as? DatasetEntity
        val dataTypes =
            intent.getSerializableExtra(DatasetView.dataTypeListIntent) as? List<DataTypeEntity>
        val dataStructures =
            intent.getSerializableExtra(DatasetView.dataStructureListIntent) as? List<DataStructureEntity>
        val accessTypes =
            intent.getSerializableExtra(DatasetView.accessTypeListIntent) as? List<AccessTypeEntity>
        if (accessTypes == null || dataTypes == null || dataStructures == null) {
            finish()
        } else {
            setContentView(R.layout.activity_dataset_form)
            setSupportActionBar(findViewById(R.id.toolbar))

            val dataTypeSpinner = findViewById<Spinner>(R.id.dataset_form_data_type_spinner)
            val dataTypeAdapter: ArrayAdapter<DataTypeEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataTypes)
            dataTypeSpinner.adapter = dataTypeAdapter
            val dataStructureTextView =
                findViewById<TextView>(R.id.dataset_form_data_structure_label_text_view)
            val dataStructureSpinner =
                findViewById<Spinner>(R.id.dataset_form_data_structure_spinner)
            dataStructureSpinner.adapter = ArrayAdapter(
                this@DatasetForm,
                android.R.layout.simple_spinner_dropdown_item,
                dataStructures
            )
            dataTypeSpinner.onItemSelectedListener = getDataTypeOnItemSelectedListener(
                dataStructureTextView,
                dataStructureSpinner
            )

            val accessTypeSpinner = findViewById<Spinner>(R.id.dataset_form_access_type_spinner)
            val accessTypeAdapter: ArrayAdapter<AccessTypeEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accessTypes)
            accessTypeSpinner.adapter = accessTypeAdapter
            accessTypeSpinner.onItemSelectedListener =
                getAccessTypeOnItemSelectedListener(datasetEntity)

            datasetEntity?.also { dataset ->
                selectSpinnerItemByValue(
                    dataTypeSpinner,
                    dataset.dataType,
                    { dt1, dt2 -> dt1.uid == dt2.uid })
                datasetEntity.dataStructure?.also { dataStructure ->
                    selectSpinnerItemByValue(
                        dataStructureSpinner,
                        dataStructure,
                        { ds1, ds2 -> ds1.uid == ds2.uid })
                }
                selectSpinnerItemByValue(
                    accessTypeSpinner,
                    dataset.accessTypeEntity,
                    { at1, at2 -> at1.uid == at2.uid })

                findViewById<EditText>(R.id.dataset_form_name_edit_text).setText(dataset.name)
                if (dataset.datasetMultiplicity == DatasetMultiplicity.MULTIPLE) findViewById<CheckBox>(
                    R.id.dataset_form_is_multiple_checkbox
                ).isChecked =
                    true
            }

            findViewById<Button>(R.id.dataset_form_create_button)
                .setOnClickListener {
                    val datasetCreateDTO = getDatasetCreateDTO(datasetEntity?.uid)
                    launch(job) {
                        sendDatasetRequest(datasetCreateDTO)
                    }
                    finish()
                }

            findViewById<Button>(R.id.dataset_form_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    protected abstract suspend fun sendDatasetRequest(datasetCreate: DatasetCreate): String?

    private fun getDataTypeOnItemSelectedListener(
        textView: TextView,
        dataStructureSpinner: Spinner
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
            } else {
                textView.visibility = View.GONE
                dataStructureSpinner.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            return
        }
    }

    private fun getAccessTypeOnItemSelectedListener(initialValue: DatasetEntity?) =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val linearLayout =
                    findViewById<LinearLayout>(R.id.dataset_form_access_type_values_linear_layout)
                linearLayout.removeAllViews()
                editTextByAccessValue.clear()
                val accessType = parent.selectedItem as AccessTypeEntity
                // assuming all fields are string, therefore not checking map values
                val existingAccessValues =
                    initialValue?.accessValues?.let { convertFromJsonStringToMap(gson, it) }
                        ?: emptyMap()
                accessType.accessFieldNameByType.keys.forEach { fieldName ->
                    addTextViewToViewGroup(fieldName, linearLayout, this@DatasetForm)
                    val editText = EditText(this@DatasetForm)
                    existingAccessValues[fieldName]?.also { editText.setText(it) }
                    linearLayout.addView(editText)
                    editTextByAccessValue[fieldName] = editText
                }
                val existingPathValues =
                    initialValue?.pathValues?.let { convertFromJsonStringToMap(gson, it) }
                        ?: emptyMap()
                accessType.pathFieldNameByType.keys.forEach { fieldName ->
                    addTextViewToViewGroup(fieldName, linearLayout, this@DatasetForm)
                    val editText = EditText(this@DatasetForm)
                    existingPathValues[fieldName]?.also { editText.setText(it) }
                    linearLayout.addView(editText)
                    editTextByPathValue[fieldName] = editText
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }

    private fun getDatasetCreateDTO(datasetUid: String?): DatasetCreate {
        val name =
            findViewById<EditText>(R.id.dataset_form_name_edit_text).text.toString().trim()
        val isMultiple =
            findViewById<CheckBox>(R.id.dataset_form_is_multiple_checkbox).isChecked
        val dataTypeSpinner = findViewById<Spinner>(R.id.dataset_form_data_type_spinner)
        val dataType = dataTypeSpinner.selectedItem as DataTypeEntity
        val dataStructureSpinner = findViewById<Spinner>(R.id.dataset_form_data_structure_spinner)
        val dataStructure = dataStructureSpinner.selectedItem as DataStructureEntity?
        val accessTypeSpinner = findViewById<Spinner>(R.id.dataset_form_access_type_spinner)
        val accessType = accessTypeSpinner.selectedItem as AccessTypeEntity

        return DatasetCreate(
            name,
            datasetUid,
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
            convertToJsonStringWithNewlines(editTextByPathValue),
            convertToJsonStringWithNewlines(editTextByAccessValue)
        )
    }

    // webapp uses newlines to display formatted json
    private fun convertToJsonStringWithNewlines(editTextByValue: Map<String, EditText>): String {
        val jsonString = gson.toJson(createValueByFieldName(editTextByValue))
        if (editTextByValue.isEmpty()) {
            return jsonString
        }
        val commaPositions = getCommaPositions(jsonString)
        val stringBuilder = StringBuilder(jsonString)

        stringBuilder.insert(jsonString.length - 1, '\n')
        commaPositions.asReversed().forEach {
            stringBuilder.insert(it + 1, '\n')
        }
        stringBuilder.insert(1, '\n')

        return stringBuilder.toString()
    }

    private fun getCommaPositions(string: String): List<Int> {
        val list = mutableListOf<Int>()

        var i = -1
        while (true) {
            i = string.indexOf(',', i + 1)
            when (i) {
                -1 -> return list
                else -> list.add(i)
            }
        }
    }

    private fun createValueByFieldName(editTextByFieldName: Map<String, EditText>) =
        editTextByFieldName.map {
            val fieldName = it.key
            val fieldValue = it.value.text.toString().trim()
            fieldName to fieldValue
        }.toMap()

    private fun <T> selectSpinnerItemByValue(
        spinner: Spinner,
        value: T,
        comparingFunction: (T, T) -> Boolean
    ) {
        val adapter: ArrayAdapter<T> = spinner.adapter as ArrayAdapter<T>
        for (position in 0 until adapter.count) {
            if (comparingFunction(adapter.getItem(position) as T, value)) {
                spinner.setSelection(position)
                return
            }
        }
    }
}
