package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.entity.DatasetPinEntity
import pl.oczadly.baltic.lsc.app.dto.dataset.DatasetBinding
import pl.oczadly.baltic.lsc.computation.ComputationApi

class ComputationTaskStart : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val computationApi = ComputationApi(MainActivity.state)

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskName = intent.getStringExtra("computationTaskName")
        val datasetPins =
            intent.getSerializableExtra("datasetPins") as? ArrayList<DatasetPinEntity>
        if (taskName == null || datasetPins == null) {
            finish()
        } else {
            setContentView(R.layout.activity_computation_task_start)
            setSupportActionBar(findViewById(R.id.toolbar))

            findViewById<TextView>(R.id.computation_task_start_task_name_text_view).text = taskName

            val requiredLinearLayout =
                findViewById<LinearLayout>(R.id.computation_task_start_required_linear_layout)
            val requiredDatasets = datasetPins.filter { it.binding == DatasetBinding.REQUIRED }
            addDatasetSpinnersToLayout(requiredDatasets, requiredLinearLayout)
            val providedLinearLayout =
                findViewById<LinearLayout>(R.id.computation_task_start_provided_linear_layout)
            val providedDatasets = datasetPins.filter { it.binding == DatasetBinding.PROVIDED }
            addDatasetSpinnersToLayout(providedDatasets, providedLinearLayout)

            findViewById<Button>(R.id.computation_task_start_create_button)
                .setOnClickListener {
//                sendStartTaskRequestAndFinish()
                    finish()
                }

            findViewById<Button>(R.id.computation_task_start_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    private fun addDatasetSpinnersToLayout(
        datasets: List<DatasetPinEntity>,
        layout: LinearLayout
    ) {
        datasets.forEach {
            val textView = TextView(applicationContext)
            textView.text = it.name
            val spinner = Spinner(applicationContext)
            // TODO: fetch data suitable for dataTypeUid
            spinner.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                listOf(it.dataTypeUid)
            )

            layout.addView(textView)
            layout.addView(spinner)
        }
    }
}