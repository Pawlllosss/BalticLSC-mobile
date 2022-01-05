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
        val taskName = intent.getStringExtra("computationTaskName")
        val taskUid = intent.getStringExtra("computationTaskUid")
        setContentView(R.layout.activity_delete_resource)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (taskName == null || taskUid == null) {
            finish()
        } else {
            findViewById<TextView>(R.id.resource_delete_message_text_view).text =
                "Are you sure you want to abort the task?"
            findViewById<TextView>(R.id.resource_delete_name_text_view).text = taskName
            findViewById<Button>(R.id.resource_delete_delete_button)
                .setOnClickListener {
                    sendAbortTaskRequestAndFinish(taskUid)
                    finish()
                }

            findViewById<Button>(R.id.resource_delete_cancel_button)
                .setOnClickListener {
                    finish()
                }
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