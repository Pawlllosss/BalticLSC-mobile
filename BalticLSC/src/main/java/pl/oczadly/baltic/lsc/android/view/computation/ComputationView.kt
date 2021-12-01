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
import pl.oczadly.baltic.lsc.android.view.computation.adapter.ComputationTaskGroupAdapter
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskEntity
import pl.oczadly.baltic.lsc.android.view.computation.entity.ComputationTaskGroup
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem
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
                return@withContext appApi.fetchApplicationShelf().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext listOf()
            }
        }
    }

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

            val tasksAndAppByAppName = groupTasksAndAppByAppName(applications, computationTasks)
            val computationTaskGroups = createComputationTaskGroups(tasksAndAppByAppName)

            val recyclerView = view.findViewById<RecyclerView>(R.id.computation_recycler_view)
            recyclerView.adapter = ComputationTaskGroupAdapter(computationTaskGroups)
        }
    }

    private fun groupTasksAndAppByAppName(
        applications: List<AppShelfItem>,
        computationTasks: List<Task>
    ): Map<String, List<Map.Entry<Task, AppShelfItem>>> {
        val applicationByAppVersionUid = applications.map { it.uid to it }.toMap()
        val computationTasksAndApps =
            computationTasks.filter { applicationByAppVersionUid.containsKey(it.releaseUid) }
                .map { it to applicationByAppVersionUid[it.releaseUid]!! }
                .toMap()
        return computationTasksAndApps.entries
            .groupBy({ it.value.unit.name }, { it })
    }

    private fun createComputationTaskGroups(tasksAndAppByAppName: Map<String, List<Map.Entry<Task, AppShelfItem>>>) =
        tasksAndAppByAppName.map {
            ComputationTaskGroup(
                it.key,
                createComputationTasks(it)
            )
        }

    private fun createComputationTasks(it: Map.Entry<String, List<Map.Entry<Task, AppShelfItem>>>) =
        it.value.map {
            val task = it.key
            val app = it.value
            ComputationTaskEntity(
                task.parameters.taskName,
                app.version,
                task.start,
                task.finish,
                task.status,
                task.parameters.priority
            )
        }
}