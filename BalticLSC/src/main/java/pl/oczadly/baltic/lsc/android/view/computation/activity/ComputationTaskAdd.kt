package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
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

class ComputationTaskAdd : AppCompatActivity(), CoroutineScope {

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
                    sendCreateTaskRequest(versionSpinner)
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
                    val defaultCluster = ClusterEntity("", "Determined by LSC")
                    it.add(defaultCluster)
                    it.addAll(clusters)
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            return
        }
    }

    private fun sendCreateTaskRequest(versionSpinner: Spinner) {
        val appReleaseEntity =
            versionSpinner.selectedItem as AppReleaseEntity
        val taskCreateDTO = getTaskCreateDTO(appReleaseEntity)
        launch(job) {
            computationService.createTask(taskCreateDTO, appReleaseEntity.releaseUid)
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
