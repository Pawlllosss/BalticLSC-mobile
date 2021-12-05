package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
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
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.android.view.computation.adapter.ComputationTaskGroupAdapter
import pl.oczadly.baltic.lsc.android.view.computation.converter.ComputationTaskEntityConverter
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskGroup
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem
import pl.oczadly.baltic.lsc.app.dto.list.AppListItem
import pl.oczadly.baltic.lsc.app.dto.list.AppRelease
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.computation.dto.Task
import pl.oczadly.baltic.lsc.lazyPromise

class ComputationView : Fragment(), CoroutineScope {

    private val job = Job()

    private val computationApi = ComputationApi(MainActivity.state)
    private val tasks by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext computationApi.fetchComputationTasks().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }

    private val appApi = AppApi(MainActivity.state)
    private val appList by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext appApi.fetchApplicationList().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }
    private val appShelf by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext appApi.fetchApplicationShelf().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }
    private val appListItemEntityConverter = AppListItemEntityConverter()
    private val appShelfEntityConverter = AppShelfEntityConverter()
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
            val applicationsList = appList.await()
            val applicationsShelf = appShelf.await()
            val computationTasks = tasks.await()
            // TODO: should support only apps present on shelf

            val appShelfEntityByReleaseUid: Map<String, AppShelfEntity> =
                createReleaseUidByAppShelfEntity(applicationsShelf)
            val tasksByApp: Map<AppListItem, List<Task>> =
                groupTasksByApp(applicationsList, computationTasks)
            val computationTaskGroups = createComputationTaskGroups(tasksByApp)

            val recyclerView = view.findViewById<RecyclerView>(R.id.computation_recycler_view)
            recyclerView.adapter =
                ComputationTaskGroupAdapter(computationTaskGroups, appShelfEntityByReleaseUid)
        }
    }

    private fun createReleaseUidByAppShelfEntity(applicationsShelf: List<AppShelfItem>) =
        applicationsShelf.map { it.uid to appShelfEntityConverter.convertFromAppShelfItemDTO(it) }
            .toMap()

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
        }

    private fun createComputationTasks(
        tasks: List<Task>,
        appReleaseByUid: Map<String, AppRelease>
    ) = tasks.map { computationTaskEntityConverter.convertFromTaskDTO(it, appReleaseByUid) }
}