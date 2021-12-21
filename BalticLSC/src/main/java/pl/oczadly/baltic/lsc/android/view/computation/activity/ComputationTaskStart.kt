package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
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
import pl.oczadly.baltic.lsc.android.util.addSpinnerToViewGroup
import pl.oczadly.baltic.lsc.android.util.addTextViewToViewGroup
import pl.oczadly.baltic.lsc.android.view.app.entity.DatasetPinEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetSpinnerEntity
import pl.oczadly.baltic.lsc.app.dto.dataset.DatasetBinding
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.computation.ComputationService
import pl.oczadly.baltic.lsc.lazyPromise

class ComputationTaskStart : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val computationService = ComputationService(ComputationApi(MainActivity.state))

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskName = intent.getStringExtra("computationTaskName")
        val taskUid = intent.getStringExtra("computationTaskUid")
        val datasetPins =
            intent.getSerializableExtra("datasetPins") as? ArrayList<DatasetPinEntity>
        val datasetShelfEntitiesByDataTypeUid =
            intent.getSerializableExtra("datasetShelfEntitiesByDataTypeUid") as? HashMap<String, List<DatasetEntity>>
        if (taskName == null || taskUid == null || datasetPins == null || datasetShelfEntitiesByDataTypeUid == null) {
            finish()
        } else {
            setContentView(R.layout.activity_computation_task_start)
            setSupportActionBar(findViewById(R.id.toolbar))

            findViewById<TextView>(R.id.computation_task_start_task_name_text_view).text = taskName

            val requiredLinearLayout =
                findViewById<LinearLayout>(R.id.computation_task_start_required_linear_layout)
            val requiredDatasets = datasetPins.filter { it.binding == DatasetBinding.REQUIRED }
            val requiredDataSetSpinners = addDatasetSpinnersToLayout(
                requiredDatasets,
                requiredLinearLayout,
                datasetShelfEntitiesByDataTypeUid
            )
            val providedLinearLayout =
                findViewById<LinearLayout>(R.id.computation_task_start_provided_linear_layout)
            val providedDatasets = datasetPins.filter { it.binding == DatasetBinding.PROVIDED }
            val providedDataSetSpinners = addDatasetSpinnersToLayout(
                providedDatasets,
                providedLinearLayout,
                datasetShelfEntitiesByDataTypeUid
            )

            findViewById<Button>(R.id.computation_task_start_start_button)
                .setOnClickListener {
                    val datasetUidByPinUid =
                        getDatasetUidByPinUid(requiredDataSetSpinners, providedDataSetSpinners)
                    sendStartTaskRequestAndFinish(taskUid, datasetUidByPinUid)
                }

            findViewById<Button>(R.id.computation_task_start_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    private fun addDatasetSpinnersToLayout(
        datasets: List<DatasetPinEntity>,
        layout: LinearLayout,
        datasetEntitiesByDataTypeUid: Map<String, List<DatasetEntity>>
    ): List<Spinner> = datasets.map { pin ->
        addTextViewToViewGroup(pin.name, layout, this)
        val dataSetEntities = datasetEntitiesByDataTypeUid[pin.dataTypeUid]?.map {
            DatasetSpinnerEntity(
                pin.uid,
                it.uid,
                it.name
            )
        } ?: emptyList()
        addSpinnerToViewGroup(dataSetEntities, layout, this)
    }

    private fun getDatasetUidByPinUid(
        requiredDataSetSpinners: List<Spinner>,
        providedDataSetSpinners: List<Spinner>
    ): Map<String, String> {
        val datasetSpinners = requiredDataSetSpinners + providedDataSetSpinners
        return datasetSpinners.map { it.selectedItem as DatasetSpinnerEntity }
                .map { it.pinUid to it.datesetUid }
                .toMap()
    }

    private fun sendStartTaskRequestAndFinish(
        taskUid: String,
        datasetUidByPinUid: Map<String, String>
    ) {
        launch(Dispatchers.Main) {
            try {
                lazyPromise {
                    withContext(Dispatchers.IO) {
                        try {
                            return@withContext computationService.startComputationTask(
                                taskUid,
                                datasetUidByPinUid
                            )
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
}