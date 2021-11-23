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

            val computationTasks = tasks.await()
            val computationTaskByReleaseUid = computationTasks.groupBy { it.releaseUid }
            val computationTaskGroups = computationTaskByReleaseUid.map {
                ComputationTaskGroup(
                    it.key,
                    it.value.map {
                        ComputationTaskEntity(
                            it.parameters.taskName,
                            "0.1",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            ComputationStatus.IN_PROGRESS,
                            it.parameters.priority
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