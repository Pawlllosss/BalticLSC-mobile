package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.computation.converter.ClusterEntityConverter
import pl.oczadly.baltic.lsc.android.view.computation.entity.ClusterEntity
import pl.oczadly.baltic.lsc.android.view.computation.service.ComputationService
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.computation.dto.TaskCreate
import kotlin.coroutines.CoroutineContext

class ComputationTaskAdd : AppCompatActivity(), CoroutineScope {

    companion object {
        private val defaultCluster = ClusterEntity("", "Determined by LSC")
    }

    private val job = Job()

    private val computationService =
        ComputationService(ComputationApi(MainActivity.state), ClusterEntityConverter())

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = intent.getSerializableExtra("appListItemEntity") as? AppListItemEntity
        if (app == null) {
            finish()
        } else {
            setContentView(R.layout.activity_computation_task_add)
            setSupportActionBar(findViewById(R.id.toolbar))

            val versionSpinner = findViewById<Spinner>(R.id.computation_task_version_spinner)
            val versionAdapter: ArrayAdapter<AppReleaseEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, app.releases)
            versionSpinner.adapter = versionAdapter

            val clusterSpinner = findViewById<Spinner>(R.id.computation_task_add_cluster_spinner)
            val clusterAdapter: ArrayAdapter<ClusterEntity> =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
            clusterSpinner.adapter = clusterAdapter

            versionSpinner.onItemSelectedListener = getVersionOnItemSelectedListener(clusterSpinner)

            findViewById<Button>(R.id.computation_task_add_additional_details_button)
                .setOnClickListener {
                    val layout =
                        findViewById<LinearLayout>(R.id.computation_task_add_additional_details_layout)
                    if (layout.visibility == View.VISIBLE) {
                        layout.visibility = View.GONE
                    } else {
                        layout.visibility = View.VISIBLE
                    }
                }

            findViewById<Button>(R.id.computation_task_add_create_button)
                .setOnClickListener {
                    sendCreateTaskRequest()
                    finish()
                }

            findViewById<Button>(R.id.computation_task_add_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }

    private fun getVersionOnItemSelectedListener(
        clusterSpinner: Spinner
    ) = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View,
            position: Int,
            id: Long
        ) {
            val appReleaseEntity = parent.selectedItem as AppReleaseEntity

            launch(Dispatchers.Main) {
                val clusters =
                    computationService.getClustersAvailableForTask(appReleaseEntity.releaseUid)
                val clusterAdapter = clusterSpinner.adapter as? ArrayAdapter<ClusterEntity>
                clusterAdapter?.let {
                    it.clear()
                    it.add(ComputationTaskAdd.defaultCluster)
                    it.addAll(clusters)
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            return
        }
    }

    private fun sendCreateTaskRequest() {
        val appReleaseEntity =
            findViewById<Spinner>(R.id.computation_task_version_spinner).selectedItem as AppReleaseEntity
        val taskCreateDTO = getTaskCreateDTO(appReleaseEntity)
        launch(job) {
            computationService.createTask(taskCreateDTO, appReleaseEntity.releaseUid)
        }
    }

    private fun getTaskCreateDTO(appReleaseEntity: AppReleaseEntity): TaskCreate {
        val taskName =
            findViewById<EditText>(R.id.computation_task_name_edit_text).text.toString().trim()
        val taskPriority = getNumberFromEditText(R.id.computation_task_priority_edit_text)
        val reservedCredits =
            getNumberFromEditText(R.id.computation_task_reserved_credits_edit_text)
        val isPrivate = findViewById<CheckBox>(R.id.computation_task_is_private_checkbox).isChecked
        val clusterSpinner = findViewById<Spinner>(R.id.computation_task_add_cluster_spinner)
        val clusterAllocation = getClusterAllocation()
        val clusterUid = (clusterSpinner.selectedItem as? ClusterEntity)?.uid ?: ComputationTaskAdd.defaultCluster.uid
        val auxStorageCredits =
            getNumberFromEditText(R.id.computation_task_add_aux_storage_credit_edit_text)
        val minCpu = getNumberFromEditText(R.id.computation_task_add_cpu_min_edit_text)
        val maxCpu = getNumberFromEditText(R.id.computation_task_add_cpu_max_edit_text)
        val minGpu = getNumberFromEditText(R.id.computation_task_add_gpu_min_edit_text)
        val maxGpu = getNumberFromEditText(R.id.computation_task_add_gpu_max_edit_text)
        val minMemory = getNumberFromEditText(R.id.computation_task_add_memory_min_edit_text)
        val maxMemory = getNumberFromEditText(R.id.computation_task_add_memory_max_text)
        val minStorage = getNumberFromEditText(R.id.computation_task_add_storage_min_edit_text)
        val maxStorage = getNumberFromEditText(R.id.computation_task_add_storage_max_edit_text)
        val failurePolicy = getFailureHandlingPolicy()

        return TaskCreate(
            taskName,
            appReleaseEntity.releaseUid,
            taskPriority,
            reservedCredits,
            isPrivate,
            emptyList(),
            clusterAllocation,
            clusterUid,
            auxStorageCredits,
            minCpu,
            maxCpu,
            minGpu,
            maxGpu,
            minMemory,
            maxMemory,
            minStorage,
            maxStorage,
            failurePolicy
        )
    }

    private fun getNumberFromEditText(viewId: Int) =
        findViewById<EditText>(viewId).text.toString().toInt()

    private fun getClusterAllocation(): String {
        val checkedRadioButtonId =
            findViewById<RadioGroup>(R.id.computation_task_add_allocation_radio_group).checkedRadioButtonId
        return when (checkedRadioButtonId) {
            R.id.computation_task_add_strong_cluster_radio_button -> "Strong"
            R.id.computation_task_add_weak_cluster_radio_button -> "Weak"
            else -> "Strong"
        }
    }

    private fun getFailureHandlingPolicy(): String {
        val checkedRadioButtonId =
            findViewById<RadioGroup>(R.id.computation_task_add_break_fh_radio_group).checkedRadioButtonId
        return when (checkedRadioButtonId) {
            R.id.computation_task_add_break_fh_radio_button -> "Break"
            R.id.computation_task_add_continue_fh_radio_button -> "Continue"
            R.id.computation_task_add_freeze_fh_radio_button -> "Freeze"
            else -> "Break"
        }
    }
}
