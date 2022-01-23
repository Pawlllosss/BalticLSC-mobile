package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.android.view.computation.adapter.ComputationTaskGroupAdapter
import pl.oczadly.baltic.lsc.android.view.computation.converter.ComputationTaskEntityConverter
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskGroup
import pl.oczadly.baltic.lsc.android.view.dataset.converter.AccessTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataStructureEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.android.view.dataset.service.DatasetService
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.computation.dto.Task
import pl.oczadly.baltic.lsc.dataset.DatasetApi
import pl.oczadly.baltic.lsc.lazyPromise

class ComputationView : Fragment(), CoroutineScope {

    private val job = Job()

    private val computationApi = ComputationApi(MainActivity.state)
    private val appService = AppService(
        AppApi(MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    private val datasetService = DatasetService(
        DatasetApi(MainActivity.state),
        DatasetEntityConverter(),
        DataTypeEntityConverter(),
        DataStructureEntityConverter(),
        AccessTypeEntityConverter(Gson())
    )

    private val computationTaskEntityConverter = ComputationTaskEntityConverter()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.computation_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch(Dispatchers.Main) {
            val swipeRefreshLayout =
                view.findViewById<SwipeRefreshLayout>(R.id.computation_swipe_refresh_layout)
            swipeRefreshLayout.isRefreshing = true
            val applicationsShelf = appService.getAppShelf()
            val applicationsList =
                appService.getOwnedAppList(appService.getAppList(), applicationsShelf)
            val computationTasks = createFetchComputationTasksPromise().value.await()
            val datasetEntities = datasetService.getDatasets()
            swipeRefreshLayout.isRefreshing = false

            val appShelfEntityByReleaseUid: MutableMap<String, AppShelfEntity> =
                createReleaseUidByAppShelfEntity(applicationsShelf)
            val tasksByApp: Map<AppListItemEntity, List<Task>> =
                groupTasksByApp(applicationsList, computationTasks)
            val computationTaskGroups = createComputationTaskGroups(tasksByApp)
            val datasetShelfEntitiesByDataTypeUid: MutableMap<String, List<DatasetEntity>> =
                createDatasetEntitiesByDataTypeUid(datasetEntities)

            val recyclerView = view.findViewById<RecyclerView>(R.id.computation_recycler_view)
            val computationTaskGroupAdapter = ComputationTaskGroupAdapter(
                computationTaskGroups,
                appShelfEntityByReleaseUid,
                datasetShelfEntitiesByDataTypeUid
            )
            recyclerView.adapter =
                computationTaskGroupAdapter

            swipeRefreshLayout.setOnRefreshListener {
                launch(job) {
                    val applicationsShelf = appService.getAppShelf()
                    val applicationsList =
                        appService.getOwnedAppList(appService.getAppList(), applicationsShelf)
                    val computationTasks = createFetchComputationTasksPromise().value.await()
                    val datasetsEntities = datasetService.getDatasets()

                    val appShelfEntityByReleaseUid: MutableMap<String, AppShelfEntity> =
                        createReleaseUidByAppShelfEntity(applicationsShelf)
                    val tasksByApp: Map<AppListItemEntity, List<Task>> =
                        groupTasksByApp(applicationsList, computationTasks)
                    val computationTaskGroups = createComputationTaskGroups(tasksByApp)
                    val datasetShelfEntitiesByDataTypeUid: MutableMap<String, List<DatasetEntity>> =
                        createDatasetEntitiesByDataTypeUid(datasetsEntities)

                    computationTaskGroupAdapter.updateData(
                        computationTaskGroups,
                        appShelfEntityByReleaseUid,
                        datasetShelfEntitiesByDataTypeUid
                    )
                    swipeRefreshLayout.isRefreshing = false
                }
            }
            Log.i("ComputationView", "View has been created: ${System.nanoTime()}")
        }
    }

    private fun createFetchComputationTasksPromise() = lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext computationApi.fetchComputationTasks().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }

    private fun createReleaseUidByAppShelfEntity(applicationsShelf: List<AppShelfEntity>) =
        applicationsShelf.map { it.releaseUid to it }
            .toMap()
            .toMutableMap()

    private fun groupTasksByApp(
        applications: List<AppListItemEntity>,
        computationTasks: List<Task>
    ): Map<AppListItemEntity, List<Task>> {
        val applicationByAppVersionUid: Map<String, AppListItemEntity> =
            applications.flatMap { app -> app.releases.map { it.releaseUid to app } }.toMap()
        val computationTasksAndApps: Map<Task, AppListItemEntity> =
            computationTasks.filter { applicationByAppVersionUid.containsKey(it.releaseUid) }
                .map { it to applicationByAppVersionUid[it.releaseUid]!! }
                .toMap()
        val tasksByAppListItemEntity = computationTasksAndApps.entries
            .groupBy({ it.value }, { it.key })
            .toMutableMap()
        applications.forEach {
            if (!tasksByAppListItemEntity.containsKey(it)) tasksByAppListItemEntity[it] =
                emptyList()
        }
        return tasksByAppListItemEntity
    }

    private fun createComputationTaskGroups(tasksByApp: Map<AppListItemEntity, List<Task>>) =
        tasksByApp.map { taskByApp ->
            val appReleaseByUid = taskByApp.key.releases.map { it.releaseUid to it }.toMap()
            ComputationTaskGroup(
                taskByApp.key,
                createComputationTasks(taskByApp.value, appReleaseByUid)
            )
        }.toMutableList()

    private fun createComputationTasks(
        tasks: List<Task>,
        appReleaseByUid: Map<String, AppReleaseEntity>
    ) = tasks.map { computationTaskEntityConverter.convertFromTaskDTO(it, appReleaseByUid) }

    private fun createDatasetEntitiesByDataTypeUid(datasetEntities: List<DatasetEntity>) =
        datasetEntities.groupBy { it.dataType.uid }.toMutableMap()
}
