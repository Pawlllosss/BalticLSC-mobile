package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.widget.Button
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
import pl.oczadly.baltic.lsc.computation.ComputationApi
import pl.oczadly.baltic.lsc.lazyPromise

class ComputationTaskAbort : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val computationApi = ComputationApi(MainActivity.state)

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskNameIntent = intent.getStringExtra("computationTaskName")
        val taskUidIntent = intent.getStringExtra("computationTaskUid")

        if (taskNameIntent == null || taskUidIntent == null) {
            finish()
        }
        setContentView(R.layout.activity_computation_task_abort)
        setSupportActionBar(findViewById(R.id.toolbar))

        val taskName = taskNameIntent!!
        val taskUid = taskUidIntent!!

        findViewById<TextView>(R.id.computation_task_abort_task_name_text_view).text = taskName
        findViewById<Button>(R.id.computation_task_abort_abort_button)
            .setOnClickListener {
                sendAbortTaskRequestAndFinish(taskUid)
                finish()
            }

        findViewById<Button>(R.id.computation_task_abort_cancel_button)
            .setOnClickListener {
                finish()
            }
    }

    private fun sendAbortTaskRequestAndFinish(taskUid: String) {
        launch(Dispatchers.Main) {
            try {
                lazyPromise {
                    withContext(Dispatchers.IO) {
                        try {
                            return@withContext computationApi.abortComputationTask(taskUid)
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