package pl.oczadly.baltic.lsc.android.view.computation

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
import pl.oczadly.baltic.lsc.android.view.computation.adapter.ComputationTaskGroupAdapter
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskGroup
import pl.oczadly.baltic.lsc.app.AppApi
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
    private val apps by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext appApi.fetchApplicationList().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }
    private val appListItemEntityConverter = AppListItemEntityConverter()

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
            val applications = apps.await()
            val computationTasks = tasks.await()

            val tasksByApp: Map<AppListItem, List<Task>> =
                groupTasksByApp(applications, computationTasks)
            val computationTaskGroups = createComputationTaskGroups(tasksByApp)

            val recyclerView = view.findViewById<RecyclerView>(R.id.computation_recycler_view)
            recyclerView.adapter = ComputationTaskGroupAdapter(computationTaskGroups)
        }
    }

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
    ) =
        tasks.map {
            ComputationTaskEntity(
                it.parameters.taskName,
                appReleaseByUid[it.releaseUid]?.version ?: "",
                it.start,
                it.finish,
                it.status,
                it.parameters.priority
            )
        }
}