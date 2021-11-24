package pl.oczadly.baltic.lsc.android.view.computation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.computation.dto.ComputationApi
import pl.oczadly.baltic.lsc.computation.dto.ComputationStatus
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
            val applicationByAppVersionUid = applications.map{ it.uid to it}.toMap()

            val computationTasks = tasks.await()
            val computationTasksAndApps =
                computationTasks.filter { applicationByAppVersionUid.containsKey(it.releaseUid) }
                    .map { it to applicationByAppVersionUid[it.releaseUid]!! }
                    .toMap()
            val tasksByAppName = computationTasksAndApps.entries
                .groupBy({ it.value.unit.name }, { it })
            val computationTaskGroups = tasksByAppName.map {
                ComputationTaskGroup(
                    it.key,
                    it.value.map {
                        ComputationTaskEntity(
                            it.key.parameters.taskName,
                            "0.1",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            ComputationStatus.IN_PROGRESS,
                            it.key.parameters.priority
                        )
                    })
            }

            val recyclerView = view.findViewById<RecyclerView>(R.id.computation_recycler_view)
            recyclerView.adapter =
                ComputationAdapter(
                    computationTaskGroups, context!!
                )
        }
    }
}