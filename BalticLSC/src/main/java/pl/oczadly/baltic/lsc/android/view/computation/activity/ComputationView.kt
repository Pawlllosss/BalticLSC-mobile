package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.AppService
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.android.view.computation.adapter.ComputationTaskGroupAdapter
import pl.oczadly.baltic.lsc.android.view.computation.converter.ComputationTaskEntityConverter
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskGroup
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetShelfEntity
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem
import pl.oczadly.baltic.lsc.app.dto.list.AppListItem
import pl.oczadly.baltic.lsc.app.dto.list.AppRelease
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.computation.dto.Task
import pl.oczadly.baltic.lsc.dataset.DatasetApi
import pl.oczadly.baltic.lsc.dataset.dto.DatasetShelfItem
import pl.oczadly.baltic.lsc.lazyPromise

class ComputationView : Fragment(), CoroutineScope {

    private val job = Job()

    private val computationApi = ComputationApi(MainActivity.state)
    private val appService = AppService(AppApi(MainActivity.state))

    private val datasetApi = DatasetApi(MainActivity.state)
    private val datasetShelf by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext datasetApi.fetchDatasetShelf().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }

    private val appListItemEntityConverter = AppListItemEntityConverter()
    private val appShelfEntityConverter = AppShelfEntityConverter()
    private val computationTaskEntityConverter = ComputationTaskEntityConverter()
    private val datasetShelfEntityConverter = DatasetShelfEntityConverter()

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
            val applicationsList = appService.createFetchAppListPromise().value.await()
            val applicationsShelf = appService.createFetchAppShelfPromise().value.await()
            val computationTasks = createFetchComputationTasksPromise().value.await()
            // TODO: should support only apps present on shelf
            val datasetsShelf = datasetShelf.await()

            val appShelfEntityByReleaseUid: MutableMap<String, AppShelfEntity> =
                createReleaseUidByAppShelfEntity(applicationsShelf)
            val tasksByApp: Map<AppListItem, List<Task>> =
                groupTasksByApp(applicationsList, computationTasks)
            val computationTaskGroups = createComputationTaskGroups(tasksByApp)
            val datasetShelfEntitiesByDataTypeUid: MutableMap<String, List<DatasetShelfEntity>> =
                createDatasetShelfEntitiesByDataTypeUid(datasetsShelf)

            val recyclerView = view.findViewById<RecyclerView>(R.id.computation_recycler_view)
            val computationTaskGroupAdapter = ComputationTaskGroupAdapter(
                computationTaskGroups,
                appShelfEntityByReleaseUid,
                datasetShelfEntitiesByDataTypeUid
            )
            recyclerView.adapter =
                computationTaskGroupAdapter

            val swipeRefreshLayout =
                view.findViewById<SwipeRefreshLayout>(R.id.computation_swipe_refresh_layout)
            swipeRefreshLayout.setOnRefreshListener {
                launch(job) {
                    val applicationsList = appService.createFetchAppListPromise().value.await()
                    val applicationsShelf = appService.createFetchAppShelfPromise().value.await()
                    val computationTasks = createFetchComputationTasksPromise().value.await()

                    val appShelfEntityByReleaseUid: MutableMap<String, AppShelfEntity> =
                        createReleaseUidByAppShelfEntity(applicationsShelf)
                    val tasksByApp: Map<AppListItem, List<Task>> =
                        groupTasksByApp(applicationsList, computationTasks)
                    val computationTaskGroups = createComputationTaskGroups(tasksByApp)
                    val datasetShelfEntitiesByDataTypeUid: MutableMap<String, List<DatasetShelfEntity>> =
                        createDatasetShelfEntitiesByDataTypeUid(datasetsShelf)

                    computationTaskGroupAdapter.updateData(
                        computationTaskGroups,
                        appShelfEntityByReleaseUid,
                        datasetShelfEntitiesByDataTypeUid
                    )
                    swipeRefreshLayout.isRefreshing = false
                }

            }
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

    private fun createReleaseUidByAppShelfEntity(applicationsShelf: List<AppShelfItem>) =
        applicationsShelf.map { it.uid to appShelfEntityConverter.convertFromAppShelfItemDTO(it) }
            .toMap()
            .toMutableMap()

    private fun groupTasksByApp(
        applications: List<AppListItem>,
        computationTasks: List<Task>
    ): Map<AppListItem, List<Task>> {
        val applicationByAppVersionUid: Map<String, AppListItem> =
            applications.flatMap { app -> app.releases.map { it.uid to app } }.toMap()
        val computationTasksAndApps: Map<Task, AppListItem> =
            computationTasks.filter { applicationByAppVersionUid.containsKey(it.releaseUid) }
                .map { it to applicationByAppVersionUid[it.releaseUid]!! }
                .toMap()
        return computationTasksAndApps.entries
            .groupBy({ it.value }, { it.key })
    }

    private fun createComputationTaskGroups(tasksByApp: Map<AppListItem, List<Task>>) =
        tasksByApp.map { taskByApp ->
            val appReleaseByUid = taskByApp.key.releases.map { it.uid to it }.toMap()
            ComputationTaskGroup(
                appListItemEntityConverter.convertFromAppListItemDTO(taskByApp.key),
                createComputationTasks(taskByApp.value, appReleaseByUid)
            )
        }.toMutableList()

    private fun createComputationTasks(
        tasks: List<Task>,
        appReleaseByUid: Map<String, AppRelease>
    ) = tasks.map { computationTaskEntityConverter.convertFromTaskDTO(it, appReleaseByUid) }

    private fun createDatasetShelfEntitiesByDataTypeUid(datasetsShelf: List<DatasetShelfItem>) =
        datasetsShelf.groupBy({ it.dataTypeUid },
            { datasetShelfEntityConverter.convertFromDatasetShelfItemDTO(it) })
            .toMutableMap()
}
