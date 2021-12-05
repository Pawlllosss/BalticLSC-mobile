package pl.oczadly.baltic.lsc.android.view.computation.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.entity.DatasetPinEntity
import pl.oczadly.baltic.lsc.computation.ComputationApi

class ComputationTaskStart : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val computationApi = ComputationApi(MainActivity.state)

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskNameIntent = intent.getStringExtra("computationTaskName")
        val datasetPinsIntent =
            intent.getSerializableExtra("datasetPins") as? ArrayList<DatasetPinEntity>
        if (taskNameIntent == null || datasetPinsIntent == null) {
            finish()
        } else {
            setContentView(R.layout.activity_computation_task_start)
            setSupportActionBar(findViewById(R.id.toolbar))

            val taskName = taskNameIntent!!
            val datasetPins = datasetPinsIntent!!

            // TODO: create and populate spinners with proper value

            findViewById<Button>(R.id.computation_task_start_create_button)
                .setOnClickListener {
//                sendStartTaskRequestAndFinish()
                    finish()
                }

            findViewById<Button>(R.id.computation_task_start_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }
}